# -*- coding: utf-8 -*-

# Define your item pipelines here
#
# Don't forget to add your pipeline to the ITEM_PIPELINES setting
# See: http://doc.scrapy.org/en/latest/topics/item-pipeline.html
import threading
from sqlalchemy import Column
from sqlalchemy import ForeignKey
from sqlalchemy import Integer
from sqlalchemy import String
from sqlalchemy import Table
from sqlalchemy.orm import sessionmaker, Session, relationship
from sqlalchemy import create_engine, MetaData
from .database_models import Problem, Word


# this pipelines after yielding the object, pushes to th database

class SynonymsPipeline(object):
    counter = 0
    session = None

    def open_spider(self, spider):
        print("starting")
        url = 'postgresql://{}:{}@{}:{}/{}'
        url = url.format("drunkengranite", "Dudder1218", "synonymity.cu3h5plqubcl.us-west-2.rds.amazonaws.com", "5432",
                         "synonymity")

        # The return value of create_engine() is our connection object
        engine = create_engine(url, echo=True)
        # only run this if you want to reset the db
        # reinitialize_tables(engine)
        self.session = sessionmaker(bind=engine)

    def close_spider(self, spider):
        print("the total amount of items yielded is " + str(self.counter))

    def process_item(self, item, spider):
        self.counter += 1
        local_session = self.session()
        try:
            local_session.add(Problem(**item))
            local_session.commit()
            self.counter += 1
        except:
            local_session.rollback()
            raise
        finally:
            local_session.close()
        return item


def reinitialize_tables(engine):
    meta = MetaData(bind=engine)
    problem = Table('problems', meta,
                    Column('id', Integer, primary_key=True, autoincrement=True),
                    Column('word', String),
                    )

    word = Table('words', meta,
                 Column('id', Integer, primary_key=True, autoincrement=True),
                 Column('word', String),
                 Column("problem_id", Integer),
                 )
    problem.drop(engine, checkfirst=True)
    word.drop(engine, checkfirst=True)
    problem.create(engine, checkfirst=True)
    word.create(engine, checkfirst=True)
