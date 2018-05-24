package org.activiti.latera.homs.executionListeners

import org.activiti.latera.bss.executionListeners.AbstractListener
import org.activiti.engine.delegate.Expression
import org.activiti.latera.bss.http.HTTPRestProcessor
import org.activiti.engine.delegate.DelegateExecution

public class FinishOrder extends AbstractListener {

  def finishOrder(def homsUrl, def homsUser, def homsPassword, def execution) {
    def homsOrderCode = execution.getVariable('homsOrderCode')
    def homsRequestObj = [
      order: [
        state   : "done",
        done_at : String.format("%tFT%<tRZ", Calendar.getInstance(TimeZone.getTimeZone("Z"))),
        bp_state: "done"
      ]
    ]

    def httpProcessor = new HTTPRestProcessor(execution: execution, baseUrl: "$homsUrl/api/")
    httpProcessor.httpClient.auth.basic(homsUser, homsPassword)
    httpProcessor.sendRequest('put', path: "orders/$homsOrderCode", body: homsRequestObj, execution: execution)
  }

  def execute(DelegateExecution execution) {
    def homsUrl = execution.getVariable('homsUrl')
    def homsUser = execution.getVariable('homsUser')
    def homsPassword = execution.getVariable('homsPassword')

    log('/ Finishing order...', "info", execution)
    finishOrder(homsUrl, homsUser, homsPassword, execution)
    log('\\ Order finished', "info", execution)
  }
}
