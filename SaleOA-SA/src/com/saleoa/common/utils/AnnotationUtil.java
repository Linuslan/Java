package com.saleoa.common.utils;

import java.lang.annotation.Annotation;

import com.saleoa.common.annotation.Table;

public class AnnotationUtil {
	public static String getTableName(Class cls) {
		Annotation[] annotationArr = cls.getDeclaredAnnotations();
		String tableName = "";
		for(int i = 0; i < annotationArr.length; i ++) {
			Annotation annotation = annotationArr[i];
			if(annotation.annotationType().equals(Table.class)) {
				tableName = ((Table)annotation).name();
			}
		}
		return tableName;
	}
}
