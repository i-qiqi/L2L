package queue;

import java.util.Stack;

public class StackQueue {
    Stack<Integer> stack1 = new Stack<Integer>();
    Stack<Integer> stack2 = new Stack<Integer>();

    public void push(int node){
        stack1.push(node);
    }

    public int pop() throws Exception {

        //判空
        if(empty()){
            throw  new Exception("队列空");
        }

        if(stack2.empty()){
            while(!stack1.empty()){
                stack2.push(stack1.pop());
            }
        }
        return stack2.pop();
    }

    public boolean empty(){
        if(stack1.empty() && stack2.empty()) {
            return true;
        }

        return false;
    }
    public static void main(String[] args) throws Exception {
        StackQueue queue = new StackQueue();

        queue.push(4);
        queue.push(2);
        queue.push(3);

        while(!queue.empty()){
            System.out.print(queue.pop()+" ");
        }
    }
}

