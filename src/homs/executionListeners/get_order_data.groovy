package org.activiti.latera.homs.executionListeners

import org.activiti.latera.bss.executionListeners.AbstractListener
import org.activiti.engine.delegate.Expression
import org.activiti.latera.bss.http.HTTPRestProcessor

public class GetOrderData extends AbstractListener {
  
  def getOrderData(def homsUrl, def homsUser, def homsPassword, def execution) {
    def homsOrderCode = execution.getVariable('homsOrderCode')

    def httpProcessor = new HTTPRestProcessor(execution: execution, baseUrl: "$homsUrl/api/")
    httpProcessor.httpClient.auth.basic(homsUser, homsPassword)

    def homsResp = httpProcessor.sendRequest('get', path: "orders/$homsOrderCode", execution: execution)
    def orderObj = homsResp["order"]
    def orderData = orderObj["data"].collectEntries{[it.key, it.value]}

    execution.setVariable("homsOrderId", orderObj["id"])
    for (e in orderObj["data"]) {
      execution.setVariable("homsOrderData" + e.key.capitalize(), orderObj["data"][e.key])
    }
    execution.setVariable("homsOrdDataBuffer", orderData)
  }

  def execute(execution) {
    def homsUrl = execution.getVariable('homsUrl')
    def homsUser = execution.getVariable('homsUser')
    def homsPassword = execution.getVariable('homsPassword')

    log('/ Receiving order data...', "info", execution)
    getOrderData(homsUrl, homsUser, homsPassword, execution)
    log('\\ Order data received', "info", execution)
  }
}
