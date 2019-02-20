package ru.liveproduction.livelib.math;

import java.util.List;
import java.util.Map;

public class TestExpressionObject {
    /**
     * Interface for create operation method of principe work with your custom objects
     * @param <K> - Your custom class
     */
    public interface OperationMethod<K> {

        /**
         * Operation with your object
         * @param args Your object
         * @return Your custom object
         * @throws Exception
         */
        K execute(K ...args) throws Exception;
    }



    public class Operation<K> {
        protected boolean haveUnaryMethod = false;
        protected OperationMethod<K> method;

        public Operation(OperationMethod<K> method){
            this.method = method;
        }

        public Operation(OperationMethod<K> method, boolean haveUnaryMethod) {
            this.haveUnaryMethod = haveUnaryMethod;
            this.method = method;
        }
    }

    /**
     * Class for controls operations
     * @param <K>
     */
    public class OperationManager<K> {
        protected String[] operationsTags;
        protected int[] operationsPriority;
        protected String[][] operationsString;
        protected Operation<K>[] operations;

        protected OperationManager(String[] operationsTags, int[] operationsPriority, String[][] operationsString, Operation<K>[] operations) {
            this.operationsTags = operationsTags;
            this.operationsPriority = operationsPriority;
            this.operationsString = operationsString;
            this.operations = operations;
        }

        public OperationManager(String[] operationsTags, String[] operationsPriority, String[][] operationsString, Operation<K>[] operations){
            this.operationsTags = operationsTags;
            this.operationsString = operationsString;
            this.operations = operations;

            this.operationsPriority = new int[operationsPriority.length];

            for (int i = 0; i < operationsPriority.length; i++) {
                int index = -1;
                for (int j = 0; j < operationsTags.length; j++) {
                    if (operationsPriority[i].equals(operationsTags[j])) {
                        index = j;
                        break;
                    }
                }
                this.operationsPriority[i] = index;
            }
        }

        public OperationManager(List<String> operationsTags, List<String> operationsPriority, Map<String, List<String>> operationsString, List<Operation<K>> operations) {
            this.operationsTags = operationsTags.toArray(new String[0]);
            this.operations = (Operation<K>[]) operations.toArray();
            this.operationsString = new String[operationsString.size()][];
            this.operationsPriority = new int[operationsPriority.size()];

            for (var obj : operationsString.entrySet()) {
                int index = operationsTags.indexOf(obj.getKey());
                if (index > -1) this.operationsString[index] = obj.getValue().toArray(new String[0]);
            }

            for (int i = 0; i < operationsPriority.size(); i++) {
                this.operationsPriority[i] = operationsTags.indexOf(operationsPriority.get(i));
            }
        }
    }


}
