package org.activiti.latera.homs.executionListeners

import org.activiti.latera.bss.executionListeners.AbstractListener
import org.activiti.engine.delegate.Expression
import org.activiti.latera.bss.http.HTTPRestProcessor

public class SaveOrderData extends AbstractListener {
  
  def saveOrderData(def homsUrl, def homsUser, def homsPassword, def execution) {
    def homsOrderCode = execution.getVariable('homsOrderCode')
    def homsRequestObj = [
      order: [
        data: [:]
      ]
    ]

    for (e in execution.getVariables()) {
      if (!e.key.startsWith('homsOrderData')) continue
      
      def dataKey = e.key.replaceFirst(/^homsOrderData/, "")
      dataKey = (dataKey.getAt(0).toLowerCase() + dataKey.substring(1)).toString()
      homsRequestObj.order.data[dataKey] = e.value
    }

    def httpProcessor = new HTTPRestProcessor(execution: execution, baseUrl: "$homsUrl/api/")
    httpProcessor.httpClient.auth.basic(homsUser, homsPassword)
    httpProcessor.sendRequest('put', path: "orders/$homsOrderCode", body: homsRequestObj, execution: execution)
  }

  def execute(execution) {
    def homsUrl = execution.getVariable('homsUrl')
    def homsUser = execution.getVariable('homsUser')
    def homsPassword = execution.getVariable('homsPassword')

    log('/ Saving order data...', "info", execution)
    saveOrderData(homsUrl, homsUser, homsPassword, execution)
    log('\\ Order data saved', "info", execution)
  }
}
