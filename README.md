Spring Itegration Example POC
URLS
Basic
/integrate
Transformer
/transform
Router(Payload)
/integrate/student
/integrate/address
Routing (Recipients)
/integrate/student/multiple/recipients
Routing (Header)
/integrate/route/header/value/student
/integrate/route/header/value/address
Filters
/integrate/filterProcess/address
/integrate/filterProcess/student

Sample Student
{
    "id" : "123",
    "name": "manoj",
    "school":"Maharashtra Vidyalaya"
}

Sample Address
{
    "street" : "Kurduwadi Road",
    "city": "Barshi",
    "state":"Maharashtra"
}
