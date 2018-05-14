package org.activiti.latera.bss.executionListeners

import org.activiti.latera.bss.executionListeners.AbstractListener
import org.slf4j.LoggerFactory

public class InitLogging extends AbstractListener {
    def initLogger(execution) {
        def processId = "${execution.getProcessDefinitionId()} (${execution.getProcessInstanceId()})"
        def logger = LoggerFactory.getLogger(processId)

        execution.setVariable("logger", logger)
    }

    def testLogger(execution) {
        log("Logger initialized", "info", execution)
    }

    def execute(execution) {
        initLogger(execution)
        testLogger(execution)
    }
}