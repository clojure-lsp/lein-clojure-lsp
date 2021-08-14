SHELL=/usr/bin/env bash

tag:
	$(eval TAG := $(filter-out $@,$(MAKECMDGOALS)))
	git fetch origin
	git pull origin HEAD
	sed -i "s/lein-clojure-lsp \"[0-9]\+.[0-9]\+.[0-9]\+.*\"/lein-clojure-lsp \"$(TAG)\"/g" project.clj
	sed -i "s/## Unreleased/## Unreleased\n\n## $(TAG)/g" CHANGELOG.md
	git add project.clj CHANGELOG.md
	git commit -m "Release: $(TAG)"
	git tag $(TAG)
	git push origin HEAD
	git push origin --tags

deploy:
	lein deploy clojars

# Allow args to make commands
%:
	@:

.PHONY: tag
