package kamenev.delivery.auth.filter;

//public class RequestWrapper extends HttpServletRequestWrapper {
//
//    private final Map<String, String> headers = new HashMap<>();
//
//    private RequestWrapper(HttpServletRequest request) {
//        super(request);
//    }
//
//    public static RequestWrapper of(HttpServletRequest request) {
//        return new RequestWrapper(request);
//    }
//
//    public void addHeader(String name, String val) {
//        headers.put(name, val);
//    }
//
//    @Override
//    public String getHeader(String name) {
//        String headerValue = super.getHeader(name);
//        if (headers.containsKey(name)) {
//            headerValue = headers.get(name);
//        }
//        return headerValue;
//    }
//
//    @Override
//    public Enumeration<String> getHeaders(String name) {
//        List<String> values = Collections.list(super.getHeaders(name));
//        if (headers.containsKey(name)) {
//            values.add(headers.get(name));
//        }
//        return Collections.enumeration(values);
//    }
//
//    @Override
//    public Enumeration<String> getHeaderNames() {
//        List<String> names = Collections.list(super.getHeaderNames());
//        names.addAll(headers.keySet());
//        return Collections.enumeration(names);
//    }
//}
