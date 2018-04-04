package main.java;

import java.util.List;

/**
 * 2. Add Two Numbers
 * You are given two non-empty linked lists representing two non-negative integers. The digits are stored in reverse order and each of their nodes contain a single digit. Add the two numbers and return it as a linked list.
 * You may assume the two numbers do not contain any leading zero, except the number 0 itself.
 * <p>
 * Input: (2 -> 4 -> 3) + (5 -> 6 -> 4)
 * Output: 7 -> 0 -> 8
 */

public class AddTwoNumbers {

    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        ListNode listNode = new ListNode(-1);
        ListNode currentNode = listNode;
        int carry = 0;
        while (l1 != null || l2 != null) {
            if (l1 != null) {
                if (l2 != null) {
                    currentNode.next = new ListNode((l1.val + l2.val + carry) % 10);
                    carry = (l1.val + l2.val + carry) / 10;
                    l2 = l2.next;
                } else {
                    currentNode.next = new ListNode((l1.val + carry) % 10);
                    carry = (l1.val + carry) / 10;
                }
                l1 = l1.next;
            } else {
                if (l2 != null) {
                    currentNode.next = new ListNode((l2.val + carry) % 10);
                    carry = (l2.val + carry) / 10;
                    l2 = l2.next;
                }
            }
            currentNode = currentNode.next;
        }
        if (carry != 0) {
            currentNode.next = new ListNode(carry);
        }
        return listNode.next;
    }

    public class ListNode {
        int val;
        ListNode next;

        ListNode(int x) {
            val = x;
        }
    }
}


