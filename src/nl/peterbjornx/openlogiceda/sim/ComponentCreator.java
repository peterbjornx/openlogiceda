package nl.peterbjornx.openlogiceda.sim;/*
Part of OpenLogicEDA
Copyright (C) 2017 Peter Bosch

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
*/

import nl.peterbjornx.openlogiceda.model.Component;
import nl.peterbjornx.openlogiceda.util.SimulationException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;


/**
 * @author Peter Bosch
 */
public class ComponentCreator {
    private HashMap<String, Class<Component>>  componentCache = new HashMap<>();

    private Class<Component> resolveClass(String name) throws ClassNotFoundException {
        Class<Component> out = componentCache.get(name);
        if ( out == null ) {
            String fqn = "nl.peterbjornx.openlogiceda.lib."+name;
            out = (Class<Component>) Class.forName(fqn);
            componentCache.put(name,out);
        }
        return out;
    }

    public Component createComponent(String name, String config) throws SimulationException{
        try {
            Class<Component> out = resolveClass(name);
            Constructor<?>[] ctors = out.getConstructors();
            String toks[] = config.length() == 0 ? new String[0] : config.split(" " );
            Constructor<?> ctor = null;
            for ( Constructor<?> _ctor : ctors ) {
                if ( _ctor.getParameterCount() != toks.length )
                    continue;
                ctor = _ctor;
            }
            if ( ctor == null )
                throw new SimulationException("Config does not match any constructor for "+name);
            Object[] params = new Object[toks.length];
            int i = 0;
            for (Class<?> a : ctor.getParameterTypes()) {
                if ( a.isAssignableFrom(Long.class))
                    params[i] = Long.parseLong(toks[i++]);
                else if ( a.isAssignableFrom(Integer.class))
                    params[i] = Integer.parseInt(toks[i++]);
                else if ( a.isAssignableFrom(String.class))
                    params[i] = toks[i++];
                else
                    throw new SimulationException("Invalid type in component constructor for "+name);
            }
            return (Component) ctor.newInstance(params);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new SimulationException("Failed to lookup component "+name);
        } catch (IllegalAccessException e) {
            throw new SimulationException("Protected constructor for component "+name);
        } catch (InstantiationException e) {
            throw new SimulationException("Failed to instantiate component "+name);
        } catch (InvocationTargetException e) {
            throw new SimulationException("Failed to invoke ctor for component "+name);
        } catch (NumberFormatException e) {
            throw new SimulationException("Failed to parse config for component "+name);
        }
    }

}
