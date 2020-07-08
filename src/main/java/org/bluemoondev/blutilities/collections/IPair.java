/*
 * Copyright (C) 2020 Blue Moon Development
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.bluemoondev.blutilities.collections;

/**
 * <strong>Project:</strong> Blutilities4j<br>
 * <strong>File:</strong> IPair.java<br>
 * <p>
 * TODO: Add description
 * </p>
 *
 * @author <a href = "https://bluemoondev.org"> Matt</a>
 */
public interface IPair<A, B> {
    
    public void setFirst(A first);
    public void setSecond(B second);
    
    public A getFirst();
    public B getSecond();
    
}
