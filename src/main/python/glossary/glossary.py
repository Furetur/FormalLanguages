from collections import defaultdict
import re


glossary = defaultdict(list)

word_regex = re.compile("[a-zA-Z\-]+")

# Populate the glossary
with open('text.txt', 'r') as f:
    for (line_index, line) in enumerate(f):
        for word in word_regex.findall(line):
            glossary[word].append(line_index + 1)

# Print the glossary
for (word, lines) in sorted(glossary.items()):
    print(f"{word}:\t{' '.join(map(str, lines))}")
