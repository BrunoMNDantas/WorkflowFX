/*-
 * Copyright (c) 2016, NGSPipes Team <ngspipes@gmail.com>
 * All rights reserved.
 *
 * This file is part of NGSPipes <http://ngspipes.github.io/>.
 *
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package jfxwfutils;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;


public class Event<T> {
	
	private final Map<Object, Consumer<T>> listners;
	
	public Event() {
		this.listners = new HashMap<>();
	}

	public void addListner(Consumer<T> listner){
		listners.put(listner, listner);
	}
	
	public void removeListner(Consumer<T> listner) {
		listners.remove(listner);
	}
	
	public void addListner(Runnable listner){
		Consumer<T> consumer = (t)->listner.run();
		listners.put(listner, consumer);
	}
	
	public void removeListner(Runnable listner){
		listners.remove(listner);
	}
	
	public void trigger(T t){
		for(Object key : listners.keySet())
			listners.get(key).accept(t);
	}
	
}
