Event vs Command:
- Event: something that has happened in the past = fact ~ kafka, stored in a DB
- Command: something that should happen in the future = request ~ REST, PlaceOrder/Rabbit

Aggregate command handlers:
- return List<event>
- this.registerEvent(e) from AbstractAggregateRoot (Spring)
- AggregateLifecycle.apply (Axon)

Policy: events triggering other events ± external calls

Commands can arrive:
- sync: REST, SOAP...
- async: Kafka, Rabbit...

Event can be applied to Aggregate in:
- same transaction - ACID - default 💖
- separate transaction

Snapshots:
- same event stream
- different event stream
- in a separate db

Validation:
- in command handler 💖
- in event processor

Event Versioning:
- Upcasting = transforming old events to new version
- Downcasting = transforming new events to old version


Can we think about creating snapshots each 1k or more events in order to save on memory?
So I record 1k events, on the 1k1 I create a snapshot, and delete the previous ones because if I don’t need them anymore…???


​​test scenarios recommended? how to test? what to test?
- integration tests < START HERE
+ time-traveling tests
+ upcaster test: can my current app read V1,v2,v3,v4,v5,v6
- unit tests targetting ONE aggregate
