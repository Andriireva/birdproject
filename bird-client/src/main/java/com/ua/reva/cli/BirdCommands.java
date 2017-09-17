package com.ua.reva.cli;

import com.ua.reva.cli.exceptions.BirdCliException;
import com.ua.reva.model.Bird;
import com.ua.reva.model.Sighting;
import com.ua.reva.services.BirdService;
import com.ua.reva.services.ManagementService;
import org.jline.terminal.Terminal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.shell.CompletionContext;
import org.springframework.shell.CompletionProposal;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ValueProviderSupport;
import org.springframework.shell.table.*;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@ShellComponent
public class BirdCommands {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy.MM.dd");
    private static final SimpleDateFormat FULL_TIME_FORMAT = new SimpleDateFormat("HH:mm:ss");

    private static final SimpleDateFormat FULL_DATE_TIME_FORMAT = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");

    private final BirdService birdService;
    private final ManagementService managementService;

    @Autowired
    public BirdCommands(BirdService birdService, ManagementService managementService) {
        this.birdService = birdService;
        this.managementService = managementService;
    }

    /**
     * Command to list birds
     *
     * Examples: "listbirds"
     *
     * @return table data that is printed in CLI format ordered by bird name alphabetical and total size
     */
    @ShellMethod(key = "listbirds", value = "List of the birds")
    public String listBirds() {
        List<Bird> birds = birdService.listBirds();
        birds.sort(Comparator.comparing(Bird::getName));
        LinkedHashMap<String, Object> headers = new LinkedHashMap<String, Object>();
        headers.put("name", "Name");
        headers.put("color", "Color");
        headers.put("weight", "Weight");
        headers.put("height", "Height");
        TableModel model = new BeanListTableModel(birds, headers);
        TableBuilder tableBuilder = new TableBuilder(model);
        return tableBuilder.build().render(150) +
                "\n" +
                "Total birds: " + birds.size();
    }


    /**
     * Command to list sighting of the bird in range od dates
     *
     * Examples: "listsightings --bird-name parrot --start-date '2017.06.01 14:02:44' --end-date '2017.10.01 14:02:44'"
     *
     * @param birdName bird name regular expression
     * @param startDate start date
     * @param endDate end date
     * @return table data that is printed in CLI format ordered by bird name and time
     */
    @ShellMethod(key = "listsightings", value = "List of the sighting")
    public Table listsightings(String birdName, String startDate, String endDate) {
        List<Sighting> sightings = birdService.listSightings(birdName, fromString(startDate), fromString(endDate));
        LinkedHashMap<String, Object> headers = new LinkedHashMap<String, Object>();
        headers.put("birdName", "Weight");
        headers.put("dateTime", "Date");
        sightings.sort(Comparator.comparing(Sighting::getBirdName).thenComparing(Comparator.comparing(Sighting::getDateTime)));
        TableModel model = new BeanListTableModel(sightings, headers);
        TableBuilder tableBuilder = new TableBuilder(model);
        return tableBuilder.addFullBorder(BorderStyle.fancy_light).build();
    }

    /**
     * Command to add bird with appropriate properties. All properties are mandatory
     *
     * Example: "addbird --name parrot --color red --height 2cm --weight 120"
     *
     * @param name name of a bird (mandatory)
     * @param color color of a bird (mandatory)
     * @param weight weight of a bird (mandatory)
     * @param height height of a bird (mandatory)
     * @return Returns success message or error in case of error
     */
    @ShellMethod(key = "addbird", value = "Add bird")
    public String addBird(String name, String color, String weight, String height) {
        Bird bird = new Bird(name, color, weight, height);
        Bird addedBird = birdService.addBird(bird);
        return "Bird '" + addedBird.getName() + "' successfully added to the database";
    }

    /**
     * Command to add sighting
     *
     * Example: "addsighting --bird-name parrot --location region1 --date 2017.09.01 --time 14:02:44"
     *
     * @param birdName name of a bird (mandatory)
     * @param location location of a bird (mandatory)
     * @param date date in format "yyyy.MM.dd" (mandatory)
     * @param time time in format "HH:mm:ss"(mandatory)
     * @return Returns success message or error in case of error
     */
    @ShellMethod(key = "addsighting", value = "Add sighting")
    public String addSighting(String birdName, String location, String date, String time) {
        Sighting sighting = new Sighting(birdName, location, combine(date, time));
        birdService.addSighting(sighting);
        return "Sighting has been added successfully";
    }

    /**
     * Command to delete the bird
     *
     * Example: "remove --name parrot"
     *
     * @param name name of the bird
     * @return Returns success message or error in case of error
     */
    @ShellMethod("Remove bird")
    public String remove(String name) {
        birdService.deleteBird(name);
        return "Bird Successfully deleted";
    }

    /**
     * Send shutdown to the server
     *
     * @return
     */
    @ShellMethod("Shutdown")
    public String shutdown(){
        managementService.shutDown();
        return "Shutdown command has been sent";
    }

    private Date fromString(String date) {
        try {
            return FULL_DATE_TIME_FORMAT.parse(date);
        } catch (ParseException e) {
            throw new BirdCliException("Cannot format time. Use format: " + FULL_TIME_FORMAT.toPattern());
        }
    }

    /**
     * Add to date time
     * @param date
     * @param time
     * @return sum of date and hours, minues, seconds of time
     */
    private Date combine(String date, String time) {
        Date dateDate = null;
        try {
            dateDate = DATE_FORMAT.parse(date);
        } catch (ParseException e) {
            throw new BirdCliException("Cannot format date. Use format: " + DATE_FORMAT.toPattern());
        }
        Date timeDate = null;
        try {
            timeDate = FULL_TIME_FORMAT.parse(time);
        } catch (ParseException e) {
            throw new BirdCliException("Cannot format time. Use format: " + FULL_TIME_FORMAT.toPattern());
        }
        LocalDateTime localDateTime = dateDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime localDateTimeTime = timeDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        localDateTime = localDateTime.plusHours(localDateTimeTime.getHour())
                .plusMinutes(localDateTimeTime.getMinute())
                .plusSeconds(localDateTimeTime.getSecond());

        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }


}
