#!/usr/bin/env bash
git add . && git commit -m "$1" && git push && gradle clean build --refresh-dependencies && docker build -t maayanlab/g2n:$2 -t maayanlab/g2n:latest . && docker push maayanlab/g2n:$2 && docker push maayanlab/g2n:latest