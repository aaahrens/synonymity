from sqlalchemy import String
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy import Table, Column, Integer, ForeignKey, Float, Boolean
from sqlalchemy.orm import relationship

Base = declarative_base()


class Problem(Base):
    __tablename__ = 'problems'
    id = Column(Integer, primary_key=True, autoincrement=True)
    word = Column('word', String)
    synonyms = relationship("Word")


class Word(Base):
    __tablename__ = 'words'
    id = Column(Integer, primary_key=True, autoincrement=True)
    word = Column(String)
    problem = Column("problem_id", ForeignKey('problems.id'))
