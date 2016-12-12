package com.luckyeve.elastic.common.mysql;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Table {
	
	public String value();

}
