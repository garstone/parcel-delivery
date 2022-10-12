TODOS:

Про JWT.
access - короткий ttl. когда токен протухает - то есть лежит
в редисе, но уже ttl исчерпан, используем RT.
Если же JWT уже не живет в редисе, то доступ запрещен

refresh - одноразовый токен с большим ttl. тоже лежит в редисе
и если совпадает, то выдаем JWT+RT

1. OrderService
- check configs
- messaging
- order lifecycle
2. IdentityService
3. k8s
4. api-gateway
- routes
- auth
5. etc
- add passwords
- add MSA patterns (circuit breaker, etc... to read about)
- HTTPS
6. Tracking Service

В конце - убрать все коменты и todos
обавить везде ининтерфейсы для спринга