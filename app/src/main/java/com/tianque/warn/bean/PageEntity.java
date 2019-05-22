package com.tianque.warn.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author ctrun on 2018/9/19.
 */
@SuppressWarnings("unused")
public class PageEntity<T> implements Serializable {
    private static final long serialVersionUID = -1232269915518589381L;

    /**
	 * Current page
	 */
    public long page = 0;

    /**
     * The total page count.
     */
    public long total = 0;

    public List<T> result;
}
