/*
 * Copyright 2002-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.transaction.annotation;

import org.springframework.transaction.TransactionDefinition;

/**
 * Enumeration that represents transaction isolation levels for use
 * with the {@link Transactional} annotation, corresponding to the
 * {@link TransactionDefinition} interface.
 *
 * @author Colin Sampaleanu
 * @author Juergen Hoeller
 * @since 1.2
 *
 * 事物的隔离级别
 * 原子性(Atomicity)：
 * 事务是数据库的逻辑工作单位，它对数据库的修改要么全部执行，要么全部不执行。
 * 一致性(Consistemcy)：
 * 事务前后，数据库的状态都满足所有的完整性约束。
 * 隔离性(Isolation)：
 * 并发执行的事务是隔离的，一个不影响一个。如果有两个事务，运行在相同的时间内，执行相同的功能，事务的隔离性将确保每一事务在系统中认为只有该事务在使用
 * 系统。这种属性有时称为串行化，为了防止事务操作间的混淆，必须串行化或序列化请求，使得在同一时间仅有一个请求用于同一数据。通过设置数据库的隔离级别，
 * 可以达到不同的隔离效果。
 * 持久性(Durability)：
 * 在事务完成以后，该事务所对数据库所作的更改便持久的保存在数据库之中，并不会被回滚。
 * 更新丢失：
 * 两个事务都同时更新一行数据，但是第二个事务却中途失败退出，导致对数据的两个修改都失效了。这是因为系统没有执行任何的锁操作，因此并发事务并没有被隔离开来。
 * 脏读：
 * 脏读又称无效数据读出。一个事务读取另外一个事务还没有提交的数据叫脏读。
 * 例如：事务T1修改了一行数据，但是还没有提交，这时候事务T2读取了被事务T1修改后的数据，之后事务T1因为某种原因Rollback了，那么事务T2读取的数据就是脏的。
 * 不可重复读：
 * 不可重复读是指在同一个事务内，两个相同的查询返回了不同的结果。
 * 例如：事务T1读取某一数据，事务T2读取并修改了该数据，T1为了对读取值进行检验而再次读取该数据，便得到了不同的结果。
 * 幻读：
 * 事务在操作过程中进行两次查询，第二次查询的结果包含了第一次查询中未出现的数据或者缺少了第一次查询中出现的数据
 * 例如：系统管理员A将数据库中所有学生的成绩从具体分数改为ABCDE等级，但是系统管理员B就在这个时候插入了一条具体分数的记录，当系统管理员A改结束后发现还有一条记
 * 录没有改过来，就好像发生了幻觉一样。这就叫幻读。
 * 以上的4种问题（更新丢失、脏读、不可重复读、幻读）都和事务的隔离级别有关。通过设置事务的隔离级别，可以避免上述问题的发生。
 */
public enum Isolation {

	/**
	 * Use the default isolation level of the underlying datastore.
	 * All other levels correspond to the JDBC isolation levels.
	 * @see java.sql.Connection
	 *
	 * 为数据源的默认隔离级别
	 */
	DEFAULT(TransactionDefinition.ISOLATION_DEFAULT),

	/**
	 * A constant indicating that dirty reads, non-repeatable reads and phantom reads
	 * can occur. This level allows a row changed by one transaction to be read by
	 * another transaction before any changes in that row have been committed
	 * (a "dirty read"). If any of the changes are rolled back, the second
	 * transaction will have retrieved an invalid row.
	 * @see java.sql.Connection#TRANSACTION_READ_UNCOMMITTED
	 *
	 * 未授权读取级别
	 *
	 * 以操作同一行数据为前提，读事务允许其他读事务和写事务，未提交的写事务禁止其他写事务（但允许其他读事务）。
	 * 此隔离级别可以防止更新丢失，但不能防止脏读、不可重复读、幻读。此隔离级别可以通过“排他写锁”实现。
	 */
	READ_UNCOMMITTED(TransactionDefinition.ISOLATION_READ_UNCOMMITTED),

	/**
	 * A constant indicating that dirty reads are prevented; non-repeatable reads
	 * and phantom reads can occur. This level only prohibits a transaction
	 * from reading a row with uncommitted changes in it.
	 * @see java.sql.Connection#TRANSACTION_READ_COMMITTED
	 *
	 * 授权读取级别
	 *
	 *
	 *
	 * 以操作同一行数据为前提，读事务允许其他读事务和写事务，未提交的写事务禁止其他读事务和写事务。
	 * 此隔离级别可以防止更新丢失、脏读，但不能防止不可重复读、幻读。此隔离级别可以通过“瞬间共享读锁”和“排他写锁”实现。
	 */
	READ_COMMITTED(TransactionDefinition.ISOLATION_READ_COMMITTED),

	/**
	 * A constant indicating that dirty reads and non-repeatable reads are
	 * prevented; phantom reads can occur. This level prohibits a transaction
	 * from reading a row with uncommitted changes in it, and it also prohibits
	 * the situation where one transaction reads a row, a second transaction
	 * alters the row, and the first transaction rereads the row, getting
	 * different values the second time (a "non-repeatable read").
	 * @see java.sql.Connection#TRANSACTION_REPEATABLE_READ
	 *
	 * 可重复读取级别
	 *
	 *
	 *
	 * 以操作同一行数据为前提，读事务禁止其他写事务（但允许其他读事务），未提交的写事务禁止其他读事务和写事务。
	 * 此隔离级别可以防止更新丢失、脏读、不可重复读，但不能防止幻读。此隔离级别可以通过“共享读锁”和“排他写锁”实现。
	 */
	REPEATABLE_READ(TransactionDefinition.ISOLATION_REPEATABLE_READ),

	/**
	 * A constant indicating that dirty reads, non-repeatable reads and phantom
	 * reads are prevented. This level includes the prohibitions in
	 * {@code ISOLATION_REPEATABLE_READ} and further prohibits the situation
	 * where one transaction reads all rows that satisfy a {@code WHERE}
	 * condition, a second transaction inserts a row that satisfies that
	 * {@code WHERE} condition, and the first transaction rereads for the
	 * same condition, retrieving the additional "phantom" row in the second read.
	 * @see java.sql.Connection#TRANSACTION_SERIALIZABLE
	 *
	 * 提供严格的事务隔离。它要求事务序列化执行，事务只能一个接着一个地执行，不能并发执行。
	 * 此隔离级别可以防止更新丢失、脏读、不可重复读、幻读。如果仅仅通过“行级锁”是无法实现事务序列化的，
	 * 必须通过其他机制保证新插入的数据不会被刚执行查询操作的事务访问到。
	 *
	 * 隔离级别越高，越能保证数据的完整性和一致性，但是对并发性能的影响也越大。
	 * 对于多数应用程序，可以优先考虑把数据库系统的隔离级别设为Read Committed。它能够避免更新丢失、脏读，而且具有较好的并发性能。
	 * 尽管它会导致不可重复读、幻读这些并发问题，在可能出现这类问题的个别场合，可以由应用程序采用悲观锁或乐观锁来控制。
	 */
	SERIALIZABLE(TransactionDefinition.ISOLATION_SERIALIZABLE);


	private final int value;


	Isolation(int value) {
		this.value = value;
	}

	public int value() {
		return this.value;
	}

}
