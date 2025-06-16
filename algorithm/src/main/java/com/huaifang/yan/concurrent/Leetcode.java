package com.huaifang.yan.concurrent;


class Solution {
    public ListNode removeNthFromEnd(ListNode head, int n) {
        if(head == null || n == 0) return head;
        ListNode p = head;

        ListNode current= head;
        ListNode pointer = null;
        while (current!=null){
            current=current.next;
            if(n!=1){
                n=n-1;
            }else {
                if(pointer==null){
                    pointer=head;
                }else {
                    pointer=pointer.next;
                }
            }
        }
        if(pointer!=null&&pointer.next!=null){
            pointer.next=pointer.next.next;
        }
        return p;
    }
    // 1,2,3,4,5

    public static void main(String[] args) {
        ListNode node=new ListNode(1);
        Solution solution=new Solution();
        System.out.println(solution.removeNthFromEnd(node,1));
    }
}

