/*
 * Copyright (C) 2020 Blue Moon Development
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.bluemoondev.blutilities.generics;

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <strong>Project:</strong> Blutilities4j<br>
 * <strong>File:</strong> GenericsUtil.java<br>
 * <p>
 * Set of utilities for working with generics
 * </p>
 *
 * @author <a href = "https://bluemoondev.org"> Matt</a>
 */
public class GenericsUtil {

	/**
	 * Get the underlying class for a type, or null if the type is a variable type.
	 * 
	 * @param  type the type
	 * @return      the underlying class
	 */
	public static Class<?> getClassForType(Type type) {
		Class<?> clazz = null;
		if (type instanceof Class) clazz = (Class) type;
		else if (type instanceof ParameterizedType) clazz = getClassForType(((ParameterizedType) type).getRawType());
		else if (type instanceof GenericArrayType) {
			Type compType = ((GenericArrayType) type).getGenericComponentType();
			Class<?> compClass = getClassForType(compType);
			if (compClass != null) clazz = Array.newInstance(compClass, 0).getClass();
		}

		return clazz;
	}

	/**
	 * Gets the actual type arguments a sub class has used to extend a generic base
	 * class
	 * 
	 * @param  baseClass  the generic base class
	 * @param  childClass the sub class
	 * @return            a list of raw classes for the actual type arguments
	 */
	public static <T> List<Class<?>> getTypeArguments(Class<T> baseClass, Class<? extends T> childClass) {
		Map<Type, Type> resolvedClasses = new HashMap<>();
		Type type = childClass;
		while (!getClassForType(type).equals(baseClass)) {
			if (type instanceof Class)
				type = ((Class) type).getGenericSuperclass();
			else {
				ParameterizedType pType = (ParameterizedType) type;
				Class<?> rawType = (Class) pType.getRawType();
				Type[] actualTypeArgs = pType.getActualTypeArguments();
				TypeVariable<?>[] typeParameters = rawType.getTypeParameters();
				for (int i = 0; i < actualTypeArgs.length; i++)
					resolvedClasses.put(typeParameters[i], actualTypeArgs[i]);
				if (!rawType.equals(baseClass))
					type = rawType.getGenericSuperclass();
			}
		}

		Type[] actualTypeArgs;
		if (type instanceof Class) actualTypeArgs = ((Class) type).getTypeParameters();
		else actualTypeArgs = ((ParameterizedType) type).getActualTypeArguments();
		List<Class<?>> typeClasses = new ArrayList<>();
		for (Type baseType : actualTypeArgs) {
			while (resolvedClasses.containsKey(baseType)) { baseType = resolvedClasses.get(baseType); }
			typeClasses.add(getClassForType(baseType));
		}

		return typeClasses;
	}

	/**
	 * Retrieves the Class of the supplied object
	 * 
	 * @param  obj Object to get class of
	 * @return     object's class
	 */
	public static <T> Class<?> getClass(T obj) {
		return obj.getClass();
	}

}
