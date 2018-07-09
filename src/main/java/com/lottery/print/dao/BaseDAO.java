package com.lottery.print.dao;

public interface BaseDAO<T> {

	public int insert(T po);

	public int update(T po);

	public T getById(Object id);
}
