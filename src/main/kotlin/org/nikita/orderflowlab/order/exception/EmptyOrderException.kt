package org.nikita.orderflowlab.order.exception

class EmptyOrderException :
    OrderDomainException("Order must contain at least one line")