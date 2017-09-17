##Bird Application
How to run and use application
- Requirements to use the application:
  - Java 8
  - Maven
- Compile: `mvn clean install`
____
###Server side
- run `mvn bird-server\pom.xml spring-boot:run "-Drun.arguments=[-port <port>],[-data <data>],[-proc_count <proc_count>]"`
- Parameters
  - **port**: positive value, default value is `3000`. Parameter is `optional`
  - **data**: path to data folder, default value is `${user.directory}/serverdata`. Parameter is `optional`
  - **proc_count**: positive value, default value is `2`. Parameter is `optional`
- Simple run: `mvn bird-server\pom.xml spring-boot:run`
____
###Client side
- run `java -jar bird-client/target/bird-client-1.0-SNAPSHOT.jar [-serverPort <port>]`
- parameters
  - **serverPort**: positive value, default value is `3000`. Parameter is `optional`
- Simple run: `java -jar bird-client/target/bird-client-1.0-SNAPSHOT.jar`

####Command Examples
- addbird command:
  - addbird --name parrot --color red --height 2cm --weight 120
  - addbird --name parrot1 --color black --height 5cm --weight 130
- listbirds command:
  - listbirds
- remove command:
  - remove --name parrot
  - remove --name parrot2
- addsighitng command:
  - listsightings --bird-name parrot --start-date '2017.06.01 14:02:44' --end-date '2017.10.01 14:02:44'
  - listsightings --bird-name parrot --start-date '2017.06.01 14:02:44' --end-date '2017.08.05 14:02:44'
- listsighting command:
  - listsightings --bird-name parrot --start-date '2017.06.01 14:02:44' --end-date '2017.10.01 14:02:44'
- shutdown command:
  - shutdown
- quit command
  - quit