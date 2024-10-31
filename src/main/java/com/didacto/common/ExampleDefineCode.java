package com.didacto.common;

public class ExampleDefineCode {

    /**
     * 외부 클래스에서 ExampleConstant.SomeConstant.SOME_DEFINE 형태로 접근
     */
    public static class SomeConstant{


        public static final int SOME_DEFINE = 10;
    }

    /**
     * 외부 클래스에서ExampleDefineCode.SomeEnum.SOME_DEFINE.getValue() 형태로 접근
     */
    public static enum SomeEnum{
        SOME_DEFINE(10);

        private final int value;

        SomeEnum(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }


    }

}
