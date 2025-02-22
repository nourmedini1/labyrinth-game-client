package com.algo.common.trie;
import java.util.*;
class TrieNode {
    Map<Character, TrieNode> children = new HashMap<>();
    boolean isEndOfWord = false;
}

public class Trie {
    private final TrieNode root;
    private final Set<String> foundWords;

    public Trie() {
        root = new TrieNode();
        foundWords = new HashSet<>();
    }

    public void insert(String word) {
        TrieNode node = root;
        for (char c : word.toCharArray()) {
            node = node.children.computeIfAbsent(c, k -> new TrieNode());
        }
        node.isEndOfWord = true;
    }

    public int containsSubword(String word) {
        if (foundWords.contains(word)) {
            return 0; // Word already scored
        }

        for (int i = 0; i < word.length(); i++) {
            TrieNode node = root;
            for (int j = i; j < word.length(); j++) {
                char c = word.charAt(j);
                if (!node.children.containsKey(c)) {
                    break;
                }
                node = node.children.get(c);
                if (node.isEndOfWord) {
                    foundWords.add(word);
                    return word.length();
                }
            }
        }
        return 0;
    }
}
