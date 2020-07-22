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
package org.bluemoondev.blutilities;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * <strong>Project:</strong> Blutilities4j<br>
 * <strong>File:</strong> Blutil.java<br>
 * <p>
 * TODO: Add description
 * </p>
 *
 * @author <a href = "https://bluemoondev.org"> Matt</a>
 */
public class Blutil {

	public static final String VERSION = "${version}";


	public static Class<?> getClassForPrimitive(Class<?> primitive) {
		if (primitive.equals(Integer.TYPE)) return Integer.class;
		if (primitive.equals(Double.TYPE)) return Double.class;
		if (primitive.equals(Float.TYPE)) return Float.class;
		if (primitive.equals(Boolean.TYPE)) return Boolean.class;
		if (primitive.equals(Character.TYPE)) return Character.class;
		if (primitive.equals(Long.TYPE)) return Long.class;
		if (primitive.equals(Short.TYPE)) return Short.class;
		if (primitive.equals(Byte.TYPE)) return Byte.class;
		return primitive;
	}
	
	

	

}
