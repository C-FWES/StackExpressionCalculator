package Stack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestStack<E> {
    private List<E> list = new ArrayList<>();

    public void push(E s) {
        list.add(s);
    }

    public E pop() {
        if (list.size() == 0) {
            return null;
        }
        int top = list.size() - 1;
        E result = list.get(top);
        list.remove(top);
        return result;
    }

    public E peek() {
        if (list.size() == 0) {
            return null;
        }
        E result = list.get(list.size() - 1);
        return result;
    }


    public int getSize() {
        return list.size();
    }


    public static void main(String[] args) {
        TestStack<Integer> testStack = new TestStack<>();
        testStack.push(1);
        testStack.push(2);
        testStack.push(3);
        System.out.println(testStack.peek());
        System.out.println(testStack.pop());
        System.out.println(testStack.pop());
        System.out.println(testStack.pop());

    }
}
