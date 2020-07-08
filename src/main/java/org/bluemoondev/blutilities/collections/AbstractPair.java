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
 * <strong>File:</strong> AbstractPair.java<br>
 * <p>
 * TODO: Add description
 * </p>
 *
 * @author <a href = "https://bluemoondev.org"> Matt</a>
 */
public class AbstractPair<A, B> implements IPair<A, B>{

    protected A a;
    protected B b;

    public AbstractPair(A first, B second) {
        this.a = first;
        this.b = second;
    }

    @Override
    public void setFirst(A first) { this.a = first; }

    @Override
    public void setSecond(B second) { this.b = second; }

    @Override
    public A getFirst() { return a; }

    @Override
    public B getSecond() { return b; }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof IPair)) return false;
        IPair other = (IPair) obj;
        return a.equals(other.getFirst()) && b.equals(other.getSecond());
    }
    
    @Override
    public String toString() {
        return "[" + a + ", " + b + "] " + super.toString();
    }

}
