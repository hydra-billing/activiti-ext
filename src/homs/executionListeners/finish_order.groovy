package org.activiti.latera.homs.executionListeners

import org.activiti.latera.bss.executionListeners.AbstractListener
import org.activiti.latera.bss.http.HTTPRestProcessor
import org.activiti.engine.delegate.DelegateExecution
import org.activiti.latera.bss.logging.Logging

class FinishOrder extends AbstractListener {

  def finishOrder(def execution, def logger) {
    def homsUrl = execution.getVariable('homsUrl')
    def homsUser = execution.getVariable('homsUser')
    def homsPassword = execution.getVariable('homsPassword')
    def homsOrderCode = execution.getVariable('homsOrderCode')

    def homsRequestObj = [
      order: [
        state   : "done",
        done_at : String.format("%tFT%<tRZ", Calendar.getInstance(TimeZone.getTimeZone("Z"))),
        bp_state: "done"
      ]
    ]

    def httpProcessor = new HTTPRestProcessor(baseUrl: "$homsUrl/api/")
    httpProcessor.httpClient.auth.basic(homsUser, homsPassword)
    httpProcessor.sendRequest('put', path: "orders/$homsOrderCode", body: homsRequestObj, logger: logger)
  }

  void notify(DelegateExecution execution) {

    def logger = Logging.getLogger(execution)

    Logging.log('/ Finishing order...', "info", logger)
    finishOrder(execution, logger)
    Logging.log('\\ Order finished', "info", logger)
  }
}
