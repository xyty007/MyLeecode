package main.java;

import java.util.LinkedList;
import java.util.List;

/**
 * 23. Merge k Sorted Lists
 * Merge k sorted linked lists and return it as one sorted list. Analyze and describe its complexity.
 */
public class MergekSortedLists {
    public ListNode mergeKLists(ListNode[] lists) {
        List<ListNode> lists2 = new LinkedList<>();
        for (ListNode ln : lists) {
            if (ln != null) {
                lists2.add(ln);
            }
        }
        ListNode[] listsProcessed = new ListNode[lists2.size()];
        lists2.toArray(listsProcessed);
        if (listsProcessed.length <= 1) {
            return listsProcessed.length == 0 ? null : listsProcessed[0];
        }
        FailerNode faileTree = genFailerTree(listsProcessed);
        ListNode listHead = getListNodeAndReBuildTree(faileTree);
        ListNode node = listHead;
        while (true) {
            ListNode next = getListNodeAndReBuildTree(faileTree);
            if (next == null) {
                node.next = null;
                break;
            }
            node.next = next;
            node = next;
        }
        return listHead;
    }

    public FailerNode genFailerTree(ListNode[] lists) {
        FailerNode[] failers = new FailerNode[lists.length];
        for (int i = 0; i < lists.length; i++) {
            FailerNode fn = new FailerNode(null, null, lists[i], true);
            fn.corrFailNode = fn;
            failers[i] = fn;
        }
        while (true) {
            FailerNode[] oldFailers = failers;
            if (oldFailers.length <= 1) break;
            failers = new FailerNode[oldFailers.length / 2 + oldFailers.length % 2];
            for (int i = 0; i < oldFailers.length; i += 2) {
                if (i + 1 > oldFailers.length - 1) {
                    failers[i / 2] = new FailerNode(oldFailers[i], null, oldFailers[i].list, true);
                    failers[i / 2].corrFailNode = oldFailers[i].corrFailNode;
                } else {
                    if (oldFailers[i].list.val <= oldFailers[i + 1].list.val) {
                        failers[i / 2] = new FailerNode(oldFailers[i], oldFailers[i + 1], oldFailers[i].list, true);
                        failers[i / 2].corrFailNode = oldFailers[i].corrFailNode;
                    } else {
                        failers[i / 2] = new FailerNode(oldFailers[i], oldFailers[i + 1], oldFailers[i + 1].list, false);
                        failers[i / 2].corrFailNode = oldFailers[i + 1].corrFailNode;
                    }
                    oldFailers[i + 1].parent = failers[i / 2];
                }
                oldFailers[i].parent = failers[i / 2];
            }
        }
        return failers[0];
    }

    public ListNode getListNodeAndReBuildTree(FailerNode faileTree) {
        ListNode res = faileTree.list;
        if (res == null) return null;

        FailerNode changedFaileNode = faileTree.corrFailNode;
        changedFaileNode.list = changedFaileNode.list.next;
        while (true) {
            FailerNode parent = changedFaileNode.parent;
            if (parent == null) break;
            if (parent.right == null) {
                parent.list = changedFaileNode.list;
                parent.corrFailNode = changedFaileNode.corrFailNode;
                changedFaileNode = parent;
                continue;
            }
            FailerNode another = parent.leftfail ? parent.right : parent.left;
            if (changedFaileNode.list == null) {
                if (another.list == null) parent.list = null;
                else {
                    parent.corrFailNode = another.corrFailNode;
                    parent.leftfail = (parent.leftfail ? false : true);
                    parent.list = another.list;
                }
            } else if (another.list == null) {
                parent.list = changedFaileNode.list;
                parent.corrFailNode = changedFaileNode.corrFailNode;
            } else {
                if (changedFaileNode.list.val <= another.list.val) {
                    parent.list = changedFaileNode.list;
                    parent.corrFailNode = changedFaileNode.corrFailNode;
                } else {
                    parent.corrFailNode = another;
                    parent.corrFailNode = another.corrFailNode;
                    parent.leftfail = (parent.leftfail ? false : true);
                    parent.list = another.list;
                }
            }
            changedFaileNode = parent;
        }

        return res;
    }

    public class FailerNode {
        boolean leftfail;
        FailerNode left;
        FailerNode right;
        FailerNode parent;
        ListNode list;
        FailerNode corrFailNode;

        FailerNode(FailerNode left, FailerNode right, ListNode list, boolean leftfail) {
            this.left = left;
            this.right = right;
            this.list = list;
            this.leftfail = leftfail;
        }
    }

    public static void main(String[] args) {
        MergekSortedLists mergekSortedLists = new MergekSortedLists();
        ListNode l1 = new ListNode(-1);
        l1.next = new ListNode(1);
        ListNode l2 = new ListNode(-3);
        l2.next = new ListNode(1);
        l2.next.next = new ListNode(4);
        ListNode l3 = new ListNode(-2);
        l3.next = new ListNode(-1);
        l3.next.next = new ListNode(0);
        l3.next.next.next = new ListNode(2);
        ListNode[] list = {l1, l2, l3};
        ListNode res = mergekSortedLists.mergeKLists(list);
        while (res!=null){
            System.out.print(res.val+",");
            res=res.next;
        }
    }
}

class ListNode {
    int val;
    ListNode next;

    ListNode(int x) {
        val = x;
    }
}
