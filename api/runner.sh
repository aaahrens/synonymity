#!/bin/bash

gradle clean
gradle fatty
docker build -t drunkengranite/synonymity .
docker push drunkengranite/synonymity