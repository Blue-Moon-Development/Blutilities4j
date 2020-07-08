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
package org.bluemoondev.blutilities.collections;

/**
 * <strong>Project:</strong> Blutilities4j<br>
 * <strong>File:</strong> Pair.java<br>
 * <p>
 * TODO: Add description
 * </p>
 *
 * @author <a href = "https://bluemoondev.org"> Matt</a>
 */
public class Pair<A> extends AbstractPair<A, A> {

    public Pair(A first, A second) {
        super(first, second);
    }

    public Pair(A value) {
        this(value, value);
    }
    
    @Override
    public String toString() {
        return "Pair " + super.toString();
    }

}
