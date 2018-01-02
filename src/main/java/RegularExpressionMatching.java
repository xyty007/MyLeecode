package main.java;

import java.util.HashMap;
import java.util.Map;

public class RegularExpressionMatching {

    public Map<String, KMPMatchWithDot> map;

    public RegularExpressionMatching() {
        map = new HashMap<>();
    }

    public boolean isMatch(String s, String p) {
        SubPatternNode patternList = preProcessPattern(p);
        if (matchRest(s, patternList)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean matchRest(String s, SubPatternNode headNode) {
        if (headNode == null) {
            //no node Remaining
            return s.length() == 0 ? true : false;
        } else if (headNode.next == null) {
            //headNode is the last node
            return fullMatch(s, headNode);
        } else if (!headNode.repeatable) {
            //headNode is not the last node, and headNode is not repeatable
            KMPMatchWithDot km = getKMP(headNode.s);
            int[] matchIndex = km.findNext(s);
            if (matchIndex.length <= 0 || matchIndex[0] > 0) {
                //can't match node, or can't match from the beginning
                return false;
            } else if (matchIndex[1] >= s.length()) {
                //string no remaining
                return findNextUnRepeat(headNode.next) == null ? true : false;
            } else {
                return matchRest(s.substring(matchIndex[1]), headNode.next);
            }
        } else {
            //headNode is not the last node, and headNode is repeatable
            SubPatternNode unRepeatNode = findNextUnRepeat(headNode);
            if (unRepeatNode == null) {
                //all node remains is Rep
                return fullMatch(s, headNode);
            } else if (unRepeatNode.next == null) {
                //found a unRep node, and the unRep node is the last node
                return unRepeatNode.s.length() <= s.length()
                        && fullMatch(s.substring(0, s.length() - unRepeatNode.s.length()), headNode)
                        && fullMatch(s.substring(s.length() - unRepeatNode.s.length()), unRepeatNode);
            } else {
                //found a unRep node, and the unRep node is not last
                int watershed = 0; //the bound start for common match
                KMPMatchWithDot km = getKMP(unRepeatNode.s);
                while (watershed < s.length()) {
                    int[] matchIndex = km.findNext(s.substring(watershed));
                    if (matchIndex.length <= 0) {
                        return false;
                    } else {
                        if (fullMatch(s.substring(0, watershed + matchIndex[0]), headNode)) {
                            if (watershed + matchIndex[1] >= s.length()) {
                                //no string remains after the unRep node match over but not last node
                                return findNextUnRepeat(unRepeatNode.next) == null ? true : false;
                            } else if (matchRest(s.substring(watershed + matchIndex[1]), unRepeatNode.next)) {
                                return true;
                            } else {
                                //match fail may cause of repeated node match too few chars
                                watershed = watershed + matchIndex[0] + 1;
                                continue;
                            }
                        } else {
                            return false;
                        }
                    }
                }
            }
        }
        return false;
    }

    public SubPatternNode findNextUnRepeat(SubPatternNode node) {
        while (node != null && node.repeatable) {
            node = node.next;
        }
        return node;
    }

    public boolean fullMatch(String s, SubPatternNode node) {
        if (node.repeatable) {
            int next = 0;
            while (node != null && node.repeatable) {
                if (node.c == '.') {
                    return true;
                } else {
                    while (next < s.length() && s.charAt(next) == node.c) {
                        next++;
                    }
                }
                if (next >= s.length()) {
                    return true;
                } else {
                    node = node.next;
                }
            }
            return false;
        } else {
            if (s.length() != node.s.length()) {
                return false;
            }
            KMPMatchWithDot km = getKMP(node.s);
            int[] a = km.findNext(s);
            return a.length > 0 ? true : false;
        }
    }

    public SubPatternNode preProcessPattern(String p) {
        SubPatternNode head = new SubPatternNode(null, ' ', false, null);
        SubPatternNode currentNode = head;
        for (int i = 0; i < p.length(); i++) {
            for (int j = 0; i + j < p.length(); j++) {
                //find a char repeatable
                if (p.charAt(i + j) == '*') {
                    if (j <= 1) {
                        //only one char before the *, add one node
                        SubPatternNode node = new SubPatternNode(null, p.charAt(i), true, currentNode);
                        currentNode.next = node;
                    } else {
                        //two or more char before the *, add two node (eg: abc* -> ab, c*)
                        SubPatternNode firstNode = new SubPatternNode(p.substring(i, i + j - 1), ' ', false, currentNode);
                        currentNode.next = firstNode;
                        currentNode = currentNode.next;
                        SubPatternNode secondNode = new SubPatternNode(null, p.charAt(i + j - 1), true, firstNode);
                        currentNode.next = secondNode;
                    }
                    currentNode = currentNode.next;
                    i = i + j;
                    break;
                }
                //deal with the pattern not end with *
                if (i + j >= p.length() - 1) {
                    SubPatternNode lastNode = new SubPatternNode(p.substring(i, i + j + 1), ' ', false, currentNode);
                    currentNode.next = lastNode;
                    i = i + j;
                    break;
                }
            }
        }
        head = head.next;
        return head;
    }


    public static class KMPMatchWithDot {
        public String p;
        public int[] nextList;

        public KMPMatchWithDot(String p) {
            this.p = p;
            nextList = getKMPNextListWithDot(p);
        }

        public int[] findNext(String s) {
            int base = 0;
            int offset = 0;
            while (base + offset < s.length()) {
                int currCompare = base + offset;
                if (MatchRule(s.charAt(base + offset), p.charAt(offset))) {
                    offset++;
                    if (offset >= p.length())
                        return new int[]{base, base + p.length()};
                } else {
                    offset = nextList[offset];
                    if (offset == -1) {
                        base++;
                        offset = 0;
                    } else {
                        base = currCompare - offset;
                    }
                }
            }
            return new int[]{};
        }

        private int[] getKMPNextListWithDot(String p) {
            int[] a = new int[p.length()];
            int nextToMatch = 0;
            a[0] = -1;
            for (int i = 1; i < p.length(); i++) {
                a[i] = nextToMatch;
                while (true) {
                    if (MatchRule(p.charAt(i), p.charAt(nextToMatch))) {
                        nextToMatch += 1;
                        break;
                    } else {
                        if (nextToMatch <= 0) {
                            break;
                        }
                        nextToMatch = a[nextToMatch];
                    }
                }
            }
            return a;
        }

        private boolean MatchRule(char c, char p) {
            return (p == '.' || c == p) ? true : false;
        }
    }

    public KMPMatchWithDot getKMP(String p) {
        if (map.containsKey(p)) {
            return map.get(p);
        } else {
            KMPMatchWithDot km = new KMPMatchWithDot(p);
            map.put(p, km);
            return km;
        }
    }

    public class SubPatternNode {
        String s;
        char c;
        boolean repeatable = false;
        SubPatternNode pre;
        SubPatternNode next;

        public SubPatternNode(String s, char c, boolean repeatable, SubPatternNode pre) {
            this.s = s;
            this.c = c;
            this.repeatable = repeatable;
            this.pre = pre;
        }

    }

    public static void main(String[] args) {
        RegularExpressionMatching rem = new RegularExpressionMatching();
        System.out.println(rem.isMatch("a", ".*."));
    }

}
