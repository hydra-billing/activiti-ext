package org.activiti.latera.homs.executionListeners

import org.activiti.latera.bss.executionListeners.AbstractListener
import org.activiti.engine.delegate.Expression
import org.activiti.latera.bss.http.HTTPRestProcessor

public class FinishOrder extends AbstractListener {
  private Expression homsUrl
  private Expression homsUser
  private Expression homsPassword
  
  def getParameterValue(def parameterName, def execution) {
    def parameter = this."$parameterName"
    parameter ? (String)parameter.getValue(execution) : execution.getVariable(parameterName)
  }
  
  def finishOrder(def homsUrl, def homsUser, def homsPassword, def execution) {
    def homsOrderCode = execution.getVariable('homsOrderCode')
    def homsRequestObj = [
      order: [
        state: "done",
        done_at: String.format("%tFT%<tRZ", Calendar.getInstance(TimeZone.getTimeZone("Z"))),
        bp_state: "done"
      ]
    ]

    def httpProcessor = new HTTPRestProcessor(execution: execution, baseUrl: "$homsUrl/api/")
    httpProcessor.httpClient.auth.basic(homsUser, homsPassword)
    httpProcessor.sendRequest('put', path: "orders/$homsOrderCode", body: homsRequestObj)
  }

  def execute() {
    def homsUrl = getParameterValue('homsUrl', execution)
    def homsUser = getParameterValue('homsUser', execution)
    def homsPassword = getParameterValue('homsPassword', execution)

    log '/ Finishing order...'    
    finishOrder(homsUrl, homsUser, homsPassword, execution)
    log '\\ Order finished'
  }
}
