/*
 * Copyright 2016 HuntBugs contributors
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package one.util.huntbugs.testdata;

import java.util.Arrays;

import one.util.huntbugs.registry.anno.AssertNoWarning;
import one.util.huntbugs.registry.anno.AssertWarning;

/**
 * @author lan
 *
 */
public class TestCovariantArrays {
    final Object[] array = new String[] {"1", "2"};
    static abstract class Parent {}
    static class Child extends Parent {}
    static class GrandChild extends Child {}

    @AssertWarning("ContravariantArrayStore")
    public void test() {
        array[1] = 2;
    }

    @AssertNoWarning("*")
    public void test2d() {
        Number[][] arr = new Number[1][];
        arr[0] = new Integer[10];
        System.out.println(Arrays.deepToString(arr));
    }
    
    @AssertWarning("ContravariantArrayStore")
    public void caaStore(boolean b) {
        Object[] numbers = new Integer[10];
        if(b)
            numbers = new Long[10];
        numbers[0] = "abc";
    }

    @AssertWarning("ContravariantArrayStore")
    public <T extends Number> void genericStore(T val) {
        Object[] numbers = new String[10];
        numbers[0] = val;
    }

    @AssertNoWarning("ContravariantArrayStore")
    public <T extends Number> void testNull() {
        String[] numbers = new String[10];
        numbers[0] = null;
    }
    
    // parent is Child[] array, but every non-abstract subclass of Parent is also subclass of Child
    // so no ArrayStoreException will occur in current project and we don't report this method
    @AssertNoWarning("ContravariantArrayStore")
    public void caaStoreNoReport(Parent p) {
        Parent[] parents = new Child[10];
        parents[0] = p;
    }
    
    @AssertNoWarning("*")
    public void testTernaryPhi() {
        Object[] e = new String[10];
        Object a = "1";
        for(int i=0; i<e.length; i++) {
            a = i % 2 == 0 ? a : 123;
            e[i] = a;
        }
        System.out.println(Arrays.toString(e));
    }
}
