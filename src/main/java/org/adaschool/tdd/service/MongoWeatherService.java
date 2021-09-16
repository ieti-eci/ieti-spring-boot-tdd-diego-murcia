package org.adaschool.tdd.service;

import org.adaschool.tdd.controller.weather.dto.WeatherReportDto;
import org.adaschool.tdd.exception.WeatherReportNotFoundException;
import org.adaschool.tdd.repository.WeatherReportRepository;
import org.adaschool.tdd.repository.document.GeoLocation;
import org.adaschool.tdd.repository.document.WeatherReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MongoWeatherService implements WeatherService {
    private final WeatherReportRepository repository;

    public MongoWeatherService(@Autowired WeatherReportRepository repository) {
        this.repository = repository;
    }

    @Override
    public WeatherReport report(WeatherReportDto weatherReportDto) {
        return repository.save(new WeatherReport(weatherReportDto));
    }

    @Override
    public WeatherReport findById(String id) {
        Optional<WeatherReport> optional = repository.findById(id);

        if (optional.isPresent()) {
            return optional.get();
        }

        throw new WeatherReportNotFoundException();
    }

    @Override
    public List<WeatherReport> findNearLocation(GeoLocation geoLocation, float distanceRangeInMeters) {
        Double latGT = geoLocation.getLat()-distanceRangeInMeters;
        Double latLT = geoLocation.getLat()+distanceRangeInMeters;
        Double lngGT = geoLocation.getLng()-distanceRangeInMeters;
        Double lngLT = geoLocation.getLng()+distanceRangeInMeters;
        return repository.findNearLocation(latGT,latLT,lngGT,lngLT);
    }

    @Override
    public List<WeatherReport> findWeatherReportsByName(String reporter) {
        List<WeatherReport> reportsList = new ArrayList<WeatherReport>();
        List<WeatherReport> weatherReports = repository.findAll();

        for (WeatherReport report : weatherReports) {
            if (report.getReporter().equals(reporter)) {
                reportsList.add(report);
            }
        }
        
        return reportsList;
    }
}
