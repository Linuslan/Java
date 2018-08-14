package com.saleoa.common.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Column {
	public String name();
	
	public boolean isPrimaryKey() default false;
	
	public String jdbcType() default "TEXT";
	
	public int length() default 255;
	
	public String defaultValue() default "";
	
	public boolean isNotNull() default false;
}
