# -*- coding: utf-8 -*-
import scrapy
from bs4 import BeautifulSoup
from ..database_models import Word
from ..items import SynonymsItem


class Synonyms(scrapy.Spider):
    name = "synonyms"
    start_urls = ['https://raw.githubusercontent.com/dwyl/english-words/master/words.txt']

    def parse(self, response):
        for item in response.text.split("\n"):
            # print(item)
            request = scrapy.Request(callback=self.get_synonyms_or_null,
                                     url="http://www.thesaurus.com/browse/" + item)
            request.meta['word'] = item
            yield request
        pass

    def get_synonyms_or_null(self, response):
        if "thesaurus.com/misspelling" in response.url:
            pass
        soup = BeautifulSoup(response.text, "lxml")
        # means did exist, but has no synonyms
        if soup.find("li", id="words-gallery-no-results") is not None:
            pass
        else:
            synonyms = []
            for item in soup.find_all("a"):
                if item.has_attr("data-id") & item.has_attr("data-category") & \
                        item.has_attr("data-length") & item.has_attr("data-complexity"):
                    for span in item.find_all("span", class_="text"):
                        synonym = Word(word=span.get_text())
                        synonyms.append(synonym)

            item = SynonymsItem()
            item['word'] = response.meta['word']
            item['synonyms'] = synonyms
            yield item
        pass
