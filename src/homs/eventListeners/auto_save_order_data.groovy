package org.activiti.latera.homs.eventListeners

import org.activiti.latera.bss.eventListeners.AbstractListener
import org.activiti.latera.bss.http.HTTPRestProcessor

public class AutoSaveOrderData extends AbstractListener {
  def execute() {
    if (!isSavePossible()) return
    
    def orderData = getOrderData()
    def orderDataBuffer = getOrderDataBuffer()

    if (orderData != orderDataBuffer) {
      log '/ Saving order data...'
      saveOrderData(orderData)
      saveOrderDataBuffer(orderData)
      log '\\ Order data saved'
    } else {
      log 'Order data has not changed, save not needed'
    }
  }

  def private isSavePossible() {
    execution && execution.getVariable('homsOrderCode') && getOrderDataBuffer()
  }
  
  def private getOrderData() {
    def orderData = [:]
    
    for (e in execution.getVariables()) {
      if (!e.key.startsWith('homsOrderData')) continue
      
      def dataKey = e.key.replaceFirst(/^homsOrderData/, "")
      dataKey = (dataKey.getAt(0).toLowerCase() + dataKey.substring(1)).toString()
      orderData[dataKey] = e.value
    }
    
    orderData
  }
  
  def private getOrderDataBuffer() {
    execution.getVariable('homsOrdDataBuffer')
  }
  
  def private saveOrderData(orderData) {
    def homsUrl = execution.getVariable('homsUrl')
    def homsUser = execution.getVariable('homsUser')
    def homsPassword = execution.getVariable('homsPassword')
    def homsOrderCode = execution.getVariable('homsOrderCode')
    
    def homsRequestObj = [
      order: [
        data: orderData
      ]
    ]

    def httpProcessor = new HTTPRestProcessor(execution: execution, baseUrl: "$homsUrl/api/")
    httpProcessor.httpClient.auth.basic(homsUser, homsPassword)
    httpProcessor.sendRequest('put', path: "orders/$homsOrderCode", body: homsRequestObj)
  }
  
  def private saveOrderDataBuffer(orderData) {
    execution.setVariable('homsOrdDataBuffer', orderData)
  }

  def public boolean isFailOnException() {
    true
  }
}
