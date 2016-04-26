package org.activiti.latera.homs.executionListeners

import org.activiti.latera.bss.executionListeners.AbstractListener
import org.activiti.engine.delegate.Expression
import org.activiti.latera.bss.http.HTTPRestProcessor

public class GetOrderData extends AbstractListener {
  private Expression homsUrl
  private Expression homsUser
  private Expression homsPassword
  
  def getParameterValue(def parameterName, def execution) {
    def parameter = this."$parameterName"
    parameter ? (String)parameter.getValue(execution) : execution.getVariable(parameterName)
  }
  
  def getOrderData(def homsUrl, def homsUser, def homsPassword, def execution) {
    def homsOrderCode = execution.getVariable('homsOrderCode')

    def httpProcessor = new HTTPRestProcessor(execution: execution, baseUrl: "$homsUrl/api/")
    httpProcessor.httpClient.auth.basic(homsUser, homsPassword)

    def homsResp = httpProcessor.sendRequest('get', path: "orders/$homsOrderCode")
    def orderObj = homsResp["order"]
    def orderData = orderObj["data"].collectEntries{[it.key, it.value]}

    execution.setVariable("homsOrderId", orderObj["id"])
    for (e in orderObj["data"]) {
      execution.setVariable("homsOrderData" + e.key.capitalize(), orderObj["data"][e.key])
    }
    execution.setVariable("homsOrdDataBuffer", orderData)
  }

  def execute() {
    def homsUrl = getParameterValue('homsUrl', execution)
    def homsUser = getParameterValue('homsUser', execution)
    def homsPassword = getParameterValue('homsPassword', execution)
    
    log '/ Receiving order data...'
    getOrderData(homsUrl, homsUser, homsPassword, execution)
    log '\\ Order data received'
  }
}
