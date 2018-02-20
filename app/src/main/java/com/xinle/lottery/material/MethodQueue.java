package com.xinle.lottery.material;


import com.xinle.lottery.data.Method;

import java.io.Serializable;
import java.util.LinkedList;

/**
 * Created by gan on 2018/2/20.
 * 处理常用玩法的 集合类
 */

public class MethodQueue implements Serializable
{
    private int mLimit=9; // 队列长度

    private LinkedList<Method> mLinkedList = new LinkedList<>();

    public MethodQueue(int limit)
    {
        this.mLimit = limit;
    }
    
    public void addFirst(Method method)
    {
        for (Object object : mLinkedList)
        {
            if ((method).compareTo(object) == 0)
            {
                mLinkedList.remove(object);
                mLinkedList.addFirst(method);
                return;
            }
        }
        addLimitSize(method);
    }

    /*入列：当队列大小已满时，把队未的元素remove掉*/
    private void addLimitSize(Method method)
    {
        if (mLinkedList.size() >= mLimit)
        {
            mLinkedList.removeLast();
        }
        mLinkedList.addFirst(method);
    }

    public Method get(int position)
    {
        return mLinkedList.get(position);
    }

    public Method getFirst()
    {
        return mLinkedList.getFirst();
    }

    public int size()
    {
        return mLinkedList.size();
    }
}
