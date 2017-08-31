package com.kambi.func.examples;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

public class Showcase {

    public static void main(String[] args) {

        EventChangedService eventChangedService = new EventChangedService();

        Event event = new Event();
        EventGroupRule eventGroupRule = new EventGroupRule();

        List<ImpRequestWrapper> impRequestForCustomers =
                eventChangedService.createImpRequestForCustomers(event, eventGroupRule, Arrays.asList(CustomerEnum.values()));

        sendRequest(impRequestForCustomers);
    }


    private static class EventChangedService {

        private final EventRepository eventRepository = new EventRepository();
        private final EventStartTimeDelayFilter eventStartTimeDelayFilter = new EventStartTimeDelayFilter();
        private final EventMappingRepository eventMappingRepository = new EventMappingRepository();
        private final ImpRequestFactory impRequestFactory = new ImpRequestFactory();
        private final ImpRequestRepository impRequestRepository = new ImpRequestRepository();
        private final Logger log = Logger.getLogger(EventChangedService.class.getName());

        public List<ImpRequestWrapper> createImpRequestForCustomers(Event event, EventGroupRule eventGroupRule, Collection<CustomerEnum> customers) {

            eventRepository.saveOrUpdate(event);

            if (!eventStartTimeDelayFilter.isValid(event, eventGroupRule.getMaxEventProposalDaysInAdvance())) {
                log.info("Event " + event.getEventId() + "starts too far in advance. Saving it but further proposing is postponed. ");
                return Collections.emptyList();
            }

            List<EventMapping> eventMappings = eventMappingRepository.getByEventId(event.getEventId());
            Map<CustomerEnum, EventMapping> customerMapping = getEventMappingPerCustomer(eventMappings);

            //For each customer create an appropriate ImpRequest depending on the state of the current event
            List<ImpRequestWrapper> requests = new ArrayList<>();
            for (CustomerEnum customer : customers) {
                EventMapping eventMapping = customerMapping.get(customer);
                if (eventMapping == null) {
                    ImpRequestWrapper requestWrapper = impRequestFactory.createEvent(event, eventGroupRule, customer);
                    requests.add(requestWrapper);
                    eventMappingRepository.save(EventMapping.createPendingEventMapping(event.getEventId(), customer, requestWrapper.getMessageId()));
                } else if (!eventMapping.isPending() && eventStartDateDiffers(eventMapping.getItalianData().get().getStartDate(), event.getStartDate())) {
                    ImpRequestWrapper requestWrapper = getImpRequestWrapperForUpdateEvent(eventMapping, event);
                    requests.add(requestWrapper);
                    eventMapping.setPending(requestWrapper.getMessageId());
                    eventMappingRepository.update(eventMapping);
                } else {
                    log.info("Stopping processing of event with eventId: " + event.getEventId() +
                            " for customer: " + customer.getSystemId() +
                            " because there is already an IMP request in progress, or start time is the same.");
                }
            }

            requests.forEach(impRequestRepository::save);

            return requests;
        }

        private static Map<CustomerEnum, EventMapping> getEventMappingPerCustomer(List<EventMapping> eventMappings) {
            return new HashMap<>();
        }

        private static ImpRequestWrapper getImpRequestWrapperForUpdateEvent(EventMapping eventMapping, Event event) {
            return null;
        }

        private static boolean eventStartDateDiffers(Instant a, Instant b) {
            return true;
        }
    }

    private static class ImpRequestWrapper {
        String getMessageId() {
            return "1";
        }
    }

    private static class Event {
        int getEventId() {
            return 1;
        }

        Instant getStartDate() {
            return Instant.now();
        }
    }

    private static class EventGroupRule {
        int getMaxEventProposalDaysInAdvance() {
            return 1;
        }
    }

    private static class EventRepository {
        void saveOrUpdate(Event event) {

        }
    }

    private static class EventStartTimeDelayFilter {
        boolean isValid(Event event, int days) {
            return true;
        }
    }

    private interface EventMapping {
        void setPending(String messageId);

        static EventMapping createPendingEventMapping(int eventId, CustomerEnum customer, String messageId) {
            return null;
        }

        boolean isPending();

        Optional<ItalianData> getItalianData();
    }

    private interface ItalianData {
        Instant getStartDate();
    }

    private static class EventMappingRepository {
        List<EventMapping> getByEventId(int id) {
            return Collections.emptyList();
        }

        void save(EventMapping eventMapping) {

        }

        void update(EventMapping eventMapping) {

        }
    }

    private enum CustomerEnum {
        UNIBET, PAF, EIGHT;

        int getSystemId() {
            return 1;
        }
    }

    private static class ImpRequestFactory {
        ImpRequestWrapper createEvent(Event event, EventGroupRule eventGroupRule, CustomerEnum customer) {
            return new ImpRequestWrapper();
        }
    }

    private static class ImpRequestRepository {
        void save(ImpRequestWrapper requestWrapper) {

        }
    }

    private static void sendRequest(Collection<ImpRequestWrapper> requests) {

    }
}
