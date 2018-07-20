package com.jailbird.scorch;

import android.app.Application;
import android.content.res.Resources;
import android.support.v7.graphics.Palette;

import java.util.ArrayList;

import noman.googleplaces.Place;
import noman.googleplaces.PlaceType;

/**
 * Created by adria on 11/15/2016.
 */

public interface Interests {

    public Interest Alc_Drinks = new Interest("Alcoholic Drinks", new ArrayList<String>() {{
        add(PlaceType.BAR);
        add(PlaceType.BOWLING_ALLEY);
        add(PlaceType.CASINO);
        add(PlaceType.LIQUOR_STORE);
        add(PlaceType.NIGHT_CLUB);
    }});
    public Interest Alt_News = new Interest("Alternative News", new ArrayList<String>() {{
        add(PlaceType.CAFE);
        add(PlaceType.UNIVERSITY);
        add(PlaceType.BOOK_STORE);
        add(PlaceType.LIBRARY);
    }});
    public Interest Am_Radio = new Interest("Amateur Radio", new ArrayList<String>() {{
        add(PlaceType.ELECTRONICS_STORE);
        add(PlaceType.HARDWARE_STORE);
        add(PlaceType.BOOK_STORE);
    }});
    public Interest Am_Football = new Interest("American Football", new ArrayList<String>() {{
        add(PlaceType.BAR);
        add(PlaceType.CASINO);
        add(PlaceType.GYM);
        add(PlaceType.STADIUM);
        add(PlaceType.UNIVERSITY);
    }});
    public Interest Animals = new Interest("Animals", new ArrayList<String>() {{
        add(PlaceType.VETERINARY_CARE);
        add(PlaceType.PARK);
        add(PlaceType.PET_STORE);
        add(PlaceType.ZOO);
        add(PlaceType.AQUARIUM);
    }});
    public Interest Am_Lit = new Interest("American Lit", new ArrayList<String>() {{
        add(PlaceType.LIBRARY);
        add(PlaceType.CAFE);
        add(PlaceType.SCHOOL);
        add(PlaceType.UNIVERSITY);
        add(PlaceType.BOOK_STORE);
    }});
    Interest Anime = new Interest("Anime", new ArrayList<String>() {{
        add(PlaceType.ART_GALLERY);
        add(PlaceType.MUSEUM);
        add(PlaceType.MOVIE_RENTAL);
    }});
    Interest Animation = new Interest("Animation", new ArrayList<String>() {{
        add(PlaceType.ART_GALLERY);
        add(PlaceType.MOVIE_RENTAL);
        add(PlaceType.MOVIE_THEATER);
        add(PlaceType.CAFE);
        add(PlaceType.SCHOOL);
        add(PlaceType.BOOK_STORE);
    }});

    Interest Arts = new Interest("Arts", new ArrayList<String>() {{
        add(PlaceType.ART_GALLERY);
        add(PlaceType.MUSEUM);
        add(PlaceType.FLORIST);
        add(PlaceType.PAINTER);
        add(PlaceType.UNIVERSITY);
        add(PlaceType.BOOK_STORE);
    }});
    Interest Astrology = new Interest("Astrology-Psychics", new ArrayList<String>() {{
        add(PlaceType.ART_GALLERY);
        add(PlaceType.CAFE);
        add(PlaceType.BOOK_STORE);

    }});
    Interest Astronomy = new Interest("Astronomy", new ArrayList<String>() {{
        add(PlaceType.UNIVERSITY);
        add(PlaceType.SCHOOL);
        add(PlaceType.CAMPGROUND);
        add(PlaceType.BOOK_STORE);
    }});
    Interest Aviation = new Interest("Aviation-Aerospace", new ArrayList<String>() {{
        add(PlaceType.AIRPORT);
        //add(PlaceType.CAFE);
        add(PlaceType.BOOK_STORE);
    }});
    Interest Baseball = new Interest("Baseball", new ArrayList<String>() {{
        add(PlaceType.BAR);
        add(PlaceType.CASINO);
        add(PlaceType.GYM);
        add(PlaceType.STADIUM);
        add(PlaceType.UNIVERSITY);
        add(PlaceType.PARK);
    }});
    Interest Beer = new Interest("Beer", new ArrayList<String>() {{
        add(PlaceType.BAR);
        add(PlaceType.BOWLING_ALLEY);
        add(PlaceType.CASINO);
        add(PlaceType.CAFE);
        add(PlaceType.LIQUOR_STORE);
        add(PlaceType.NIGHT_CLUB);
    }});
    Interest Board_Games = new Interest("Board Games", new ArrayList<String>() {{
        add(PlaceType.CAFE);
        add(PlaceType.CASINO);
        add(PlaceType.BOWLING_ALLEY);
        add(PlaceType.STORE);
        add(PlaceType.BOOK_STORE);
        add(PlaceType.SHOPPING_MALL);
    }});
    Interest Body_Art = new Interest("Body Art", new ArrayList<String>() {{
        add(PlaceType.NIGHT_CLUB);
        add(PlaceType.ART_GALLERY);

    }});
    Interest Books = new Interest("Books", new ArrayList<String>() {{
        add(PlaceType.BOOK_STORE);
        add(PlaceType.LIBRARY);
        add(PlaceType.CAFE);
        add(PlaceType.SCHOOL);
        add(PlaceType.UNIVERSITY);
    }});
    Interest Botany = new Interest("Botany", new ArrayList<String>() {{
        add(PlaceType.PARK);
        add(PlaceType.ZOO);
        add(PlaceType.CAMPGROUND);
        add(PlaceType.FLORIST);
    }});
    Interest Bizarre = new Interest("Bizarre-Oddities", new ArrayList<String>() {{
        add(PlaceType.ART_GALLERY);
        add(PlaceType.AQUARIUM);
        add(PlaceType.CEMETERY);
        add(PlaceType.BOOK_STORE);
        add(PlaceType.LIBRARY);
        add(PlaceType.MUSEUM);
    }});
    Interest Buddihsm = new Interest("Buddhism", new ArrayList<String>() {{
        add(PlaceType.BOOK_STORE);
        add(PlaceType.PARK);
    }});
    Interest Camping = new Interest("Camping", new ArrayList<String>() {{
        add(PlaceType.PARK);
        add(PlaceType.CAMPGROUND);
        add(PlaceType.RV_PARK);
        add(PlaceType.LODGING);
        add(PlaceType.BICYCLE_STORE);
    }});
    Interest Cars = new Interest("Cars", new ArrayList<String>() {{
        add(PlaceType.CAR_RENTAL);
        add(PlaceType.CAR_REPAIR);
        add(PlaceType.CAR_WASH);
        add(PlaceType.PARKING);
        add(PlaceType.GAS_STATION);
        add(PlaceType.CAR_DEALER);


    }});
    Interest Cartoons = new Interest("Cartoons", new ArrayList<String>() {{
        add(PlaceType.ART_GALLERY);
        add(PlaceType.MOVIE_RENTAL);
        add(PlaceType.MOVIE_THEATER);
        add(PlaceType.CAFE);
        add(PlaceType.SCHOOL);
        add(PlaceType.BOOK_STORE);
    }});
    Interest Cats = new Interest("Cats", new ArrayList<String>() {{
        add(PlaceType.VETERINARY_CARE);
        add(PlaceType.PARK);
        add(PlaceType.PET_STORE);
        add(PlaceType.ZOO);
        add(PlaceType.AQUARIUM);
        add(PlaceType.MEAL_DELIVERY);
    }});
    Interest Celebs = new Interest("Celebrities", new ArrayList<String>() {{
        add(PlaceType.CAFE);
        add(PlaceType.SHOPPING_MALL);
        add(PlaceType.SHOE_STORE);
        add(PlaceType.DEPARTMENT_STORE);
        //add(PlaceType.ATM);
        add(PlaceType.STORE);
        add(PlaceType.BEAUTY_SALON);
        add(PlaceType.MOVIE_THEATER);
        add(PlaceType.MOVIE_RENTAL);

    }});
    Interest Charity = new Interest("Charity", new ArrayList<String>() {{
        add(PlaceType.SCHOOL);
        add(PlaceType.CAFE);
        add(PlaceType.CHURCH);
        add(PlaceType.BOOK_STORE);
        add(PlaceType.CITY_HALL);
        add(PlaceType.HOSPITAL);
    }});
    Interest Chess = new Interest("Chess", new ArrayList<String>() {{
        add(PlaceType.PARK);
        add(PlaceType.BOOK_STORE);
        add(PlaceType.LIBRARY);
    }});
    Interest Child_books = new Interest("Childrens Books", new ArrayList<String>() {{
        add(PlaceType.BOOK_STORE);
        add(PlaceType.LIBRARY);
        add(PlaceType.SCHOOL);
        add(PlaceType.SHOPPING_MALL);
    }});
    Interest Climbing = new Interest("Climbing", new ArrayList<String>() {{
        add(PlaceType.CAMPGROUND);
        add(PlaceType.RV_PARK);
        add(PlaceType.PARK);
        add(PlaceType.BICYCLE_STORE);
    }});
    Interest Collecting = new Interest("Collecting", new ArrayList<String>() {{
        add(PlaceType.ART_GALLERY);
        add(PlaceType.AQUARIUM);
        add(PlaceType.CEMETERY);
        add(PlaceType.BOOK_STORE);
        add(PlaceType.LIBRARY);
        add(PlaceType.MUSEUM);
        add(PlaceType.STORAGE);
    }});

    Interest Comedy_Movies = new Interest("Comedy Movies", new ArrayList<String>() {{
        add(PlaceType.MOVIE_THEATER);
        add(PlaceType.MOVIE_RENTAL);
        add(PlaceType.CAFE);

    }});

    Interest Com_Graphics = new Interest("Computer Graphics", new ArrayList<String>() {{
        add(PlaceType.UNIVERSITY);
        add(PlaceType.ELECTRONICS_STORE);

        add(PlaceType.CAFE);
    }});
    Interest Com_Hardware = new Interest("Computer Hardware", new ArrayList<String>() {{
        add(PlaceType.UNIVERSITY);
        add(PlaceType.ELECTRONICS_STORE);

        add(PlaceType.CAFE);
    }});
    Interest Com_Sci = new Interest("Computer Science", new ArrayList<String>() {{
        add(PlaceType.UNIVERSITY);
        add(PlaceType.ELECTRONICS_STORE);

        add(PlaceType.LIBRARY);
        add(PlaceType.SCHOOL);
    }});
    Interest Computers = new Interest("Computers", new ArrayList<String>() {{
        add(PlaceType.UNIVERSITY);
        add(PlaceType.ELECTRONICS_STORE);

        add(PlaceType.CAFE);
    }});
    Interest Con_Ed = new Interest("Continuing Education", new ArrayList<String>() {{
        add(PlaceType.LIBRARY);
        add(PlaceType.SCHOOL);
        add(PlaceType.UNIVERSITY);
        add(PlaceType.BOOK_STORE);
        add(PlaceType.CAFE);
        add(PlaceType.ELECTRONICS_STORE);
        add(PlaceType.LODGING);
        add(PlaceType.MUSEUM);
    }});
    Interest Cooking = new Interest("Cooking", new ArrayList<String>() {{
        add(PlaceType.RESTAURANT);
        add(PlaceType.STORE);
        add(PlaceType.MEAL_DELIVERY);
        add(PlaceType.MEAL_TAKEAWAY);
        add(PlaceType.BAKERY);
    }});
    Interest Crafts = new Interest("Crafts", new ArrayList<String>() {{
        add(PlaceType.STORE);
        add(PlaceType.HARDWARE_STORE);
        add(PlaceType.ELECTRONICS_STORE);
        add(PlaceType.ART_GALLERY);
        add(PlaceType.HOME_GOODS_STORE);
        add(PlaceType.PAINTER);

    }});
    Interest C_Films = new Interest("Cult Films", new ArrayList<String>() {{
        add(PlaceType.MOVIE_THEATER);
        add(PlaceType.MOVIE_RENTAL);
        add(PlaceType.CAFE);

    }});
    Interest C_Culture = new Interest("Cyberculture", new ArrayList<String>() {{
        add(PlaceType.UNIVERSITY);
        add(PlaceType.ELECTRONICS_STORE);
        add(PlaceType.MEAL_DELIVERY);
        add(PlaceType.MEAL_TAKEAWAY);
        add(PlaceType.CAFE);
    }});
    Interest Design = new Interest("Design", new ArrayList<String>() {{
        add(PlaceType.ART_GALLERY);
        add(PlaceType.MUSEUM);
        add(PlaceType.FLORIST);
        add(PlaceType.PAINTER);
        add(PlaceType.UNIVERSITY);
        add(PlaceType.BOOK_STORE);
    }});
    Interest Dogs = new Interest("Dogs", new ArrayList<String>() {{
        add(PlaceType.VETERINARY_CARE);
        add(PlaceType.PARK);
        add(PlaceType.PET_STORE);
        add(PlaceType.ZOO);
        add(PlaceType.MEAL_DELIVERY);
    }});
    Interest Drawing = new Interest("Drawing", new ArrayList<String>() {{
        add(PlaceType.ART_GALLERY);
        add(PlaceType.MUSEUM);
        add(PlaceType.FLORIST);
        add(PlaceType.PAINTER);
        add(PlaceType.UNIVERSITY);
        add(PlaceType.BOOK_STORE);
    }});
    Interest Ecology = new Interest("Ecology", new ArrayList<String>() {{
        add(PlaceType.PARK);
        add(PlaceType.CAMPGROUND);
        add(PlaceType.RV_PARK);
        add(PlaceType.BICYCLE_STORE);

    }});
    Interest Education = new Interest("Education", new ArrayList<String>() {{
        add(PlaceType.LIBRARY);
        add(PlaceType.SCHOOL);
        add(PlaceType.UNIVERSITY);
        add(PlaceType.BOOK_STORE);
        add(PlaceType.CAFE);
        add(PlaceType.ELECTRONICS_STORE);
        add(PlaceType.LODGING);
        add(PlaceType.MUSEUM);
    }});
    Interest Electronics = new Interest("Electronics", new ArrayList<String>() {{
        add(PlaceType.ELECTRONICS_STORE);
        add(PlaceType.ELECTRICIAN);
        add(PlaceType.UNIVERSITY);
        add(PlaceType.CAFE);
    }});
    Interest Environ = new Interest("Environment", new ArrayList<String>() {{
        add(PlaceType.PARK);
        add(PlaceType.CAMPGROUND);
        add(PlaceType.RV_PARK);
        add(PlaceType.BICYCLE_STORE);

    }});
    Interest Horses = new Interest("Equestrian-Horses", new ArrayList<String>() {{
        add(PlaceType.VETERINARY_CARE);
        add(PlaceType.PARK);
        add(PlaceType.PET_STORE);
        add(PlaceType.ZOO);
    }});
    Interest Fam = new Interest("Family", new ArrayList<String>() {{
        add(PlaceType.PARK);
        add(PlaceType.AQUARIUM);

        add(PlaceType.CAMPGROUND);
        add(PlaceType.HOME_GOODS_STORE);
        add(PlaceType.BAKERY);
        add(PlaceType.SCHOOL);
        add(PlaceType.BOWLING_ALLEY);
        add(PlaceType.LIBRARY);
        add(PlaceType.CLOTHING_STORE);
        add(PlaceType.SHOPPING_MALL);
        add(PlaceType.HOSPITAL);
    }});
    Interest Fantasy_Books = new Interest("Fantasy Books", new ArrayList<String>() {{
        add(PlaceType.LIBRARY);
        add(PlaceType.BOOK_STORE);
        add(PlaceType.ART_GALLERY);
    }});
    Interest Fashion = new Interest("Fashion", new ArrayList<String>() {{
        //add(PlaceType.CAFE);
        add(PlaceType.SHOPPING_MALL);
        add(PlaceType.SHOE_STORE);
        add(PlaceType.DEPARTMENT_STORE);
        //add(PlaceType.ATM);
        //add(PlaceType.STORE);
        add(PlaceType.BEAUTY_SALON);
        add(PlaceType.CLOTHING_STORE);
        add(PlaceType.HAIR_CARE);
        add(PlaceType.JEWELRY_STORE);
        //add(PlaceType.GYM);
    }});
    Interest F_Arts = new Interest("Fine Arts", new ArrayList<String>() {{
        add(PlaceType.ART_GALLERY);
        add(PlaceType.MUSEUM);
        add(PlaceType.FLORIST);
        add(PlaceType.PAINTER);
        add(PlaceType.UNIVERSITY);
        add(PlaceType.BOOK_STORE);
    }});
    Interest Fitness = new Interest("Fitness", new ArrayList<String>() {{
        add(PlaceType.GYM);
        add(PlaceType.PHARMACY);
        add(PlaceType.PHYSIOTHERAPIST);
        add(PlaceType.PARK);
        add(PlaceType.CAMPGROUND);
        add(PlaceType.SHOE_STORE);
        add(PlaceType.SPA);
    }});
    Interest Future = new Interest("Futurism", new ArrayList<String>() {{
        add(PlaceType.UNIVERSITY);
        add(PlaceType.ELECTRONICS_STORE);
        add(PlaceType.MEAL_DELIVERY);
        add(PlaceType.MEAL_TAKEAWAY);
        add(PlaceType.CAFE);
        add(PlaceType.LIBRARY);
        add(PlaceType.SCHOOL);
        add(PlaceType.BOOK_STORE);
    }});
    Interest Gadgets = new Interest("Gadgets", new ArrayList<String>() {{
        add(PlaceType.UNIVERSITY);
        add(PlaceType.ELECTRONICS_STORE);
        add(PlaceType.SHOPPING_MALL);
        add(PlaceType.ELECTRICIAN);

    }});
    Interest Gambling = new Interest("Gambling", new ArrayList<String>() {{
        add(PlaceType.CASINO);
        add(PlaceType.ATM);
        add(PlaceType.BANK);
        add(PlaceType.FINANCE);
        add(PlaceType.BOWLING_ALLEY);
        add(PlaceType.BAR);
        //add(PlaceType.JEWELRY_STORE);
    }});
    Interest Graphic_Design = new Interest("Graphic Design", new ArrayList<String>() {{
        add(PlaceType.ART_GALLERY);
        add(PlaceType.MUSEUM);
        add(PlaceType.FLORIST);
        add(PlaceType.PAINTER);
        add(PlaceType.UNIVERSITY);
        add(PlaceType.BOOK_STORE);
    }});
    Interest Hiking = new Interest("Hiking", new ArrayList<String>() {{
        add(PlaceType.PARK);
        add(PlaceType.CAMPGROUND);
        add(PlaceType.RV_PARK);
        add(PlaceType.LODGING);
        add(PlaceType.BICYCLE_STORE);
    }});
    Interest History = new Interest("History", new ArrayList<String>() {{
        add(PlaceType.LIBRARY);
        add(PlaceType.BOOK_STORE);
        add(PlaceType.CITY_HALL);
        add(PlaceType.COURTHOUSE);
        add(PlaceType.CEMETERY);
        add(PlaceType.CHURCH);
        add(PlaceType.HINDU_TEMPLE);
        add(PlaceType.SYNAGOGUE);
    }});
    Interest H_improve = new Interest("Home Improvement", new ArrayList<String>() {{
        add(PlaceType.HARDWARE_STORE);
        add(PlaceType.HOME_GOODS_STORE);
        add(PlaceType.DEPARTMENT_STORE);
        add(PlaceType.FURNITURE_STORE);
        add(PlaceType.BOOK_STORE);
        add(PlaceType.REAL_ESTATE_AGENCY);
        add(PlaceType.PLUMBER);
        add(PlaceType.ROOFING_CONTRACTOR);
        add(PlaceType.ELECTRICIAN);
    }});
    Interest Humans = new Interest("Humanitarianism", new ArrayList<String>() {{
        add(PlaceType.BOOK_STORE);
        add(PlaceType.DOCTOR);
        add(PlaceType.CHURCH);
        add(PlaceType.HINDU_TEMPLE);
        add(PlaceType.SYNAGOGUE);
        add(PlaceType.LIBRARY);
    }});
    Interest Humor = new Interest("Humor", new ArrayList<String>() {{
        add(PlaceType.MOVIE_RENTAL);
        add(PlaceType.MOVIE_THEATER);

    }});
    Interest Indie_Rock = new Interest("Indie Rock-Pop", new ArrayList<String>() {{
        add(PlaceType.UNIVERSITY);
        add(PlaceType.BAR);
        add(PlaceType.BOWLING_ALLEY);
    }});
    Interest Indie_Film = new Interest("Independent Film", new ArrayList<String>() {{
        add(PlaceType.MOVIE_THEATER);
        add(PlaceType.MOVIE_RENTAL);
    }});
    Interest Internet = new Interest("Internet", new ArrayList<String>() {{
        add(PlaceType.ELECTRONICS_STORE);
        add(PlaceType.UNIVERSITY);
        add(PlaceType.CAFE);
    }});
    Interest Investing = new Interest("Investing", new ArrayList<String>() {{
        add(PlaceType.BANK);
        add(PlaceType.ACCOUNTING);
        add(PlaceType.ATM);
        add(PlaceType.FINANCE);
        add(PlaceType.CASINO);
    }});
    Interest Journalism = new Interest("Journalism", new ArrayList<String>() {{
        add(PlaceType.LIBRARY);
        add(PlaceType.BOOK_STORE);
        add(PlaceType.UNIVERSITY);
    }});
    Interest Judaism = new Interest("Judaism", new ArrayList<String>() {{
        add(PlaceType.SYNAGOGUE);
        add(PlaceType.BOOK_STORE);
    }});
    Interest Limguistics = new Interest("Linguistics", new ArrayList<String>() {{
        add(PlaceType.BOOK_STORE);
        add(PlaceType.LIBRARY);
        add(PlaceType.UNIVERSITY);
    }});
    /*
    Interest Lit = new Interest("Literature", new ArrayList<String>() {{
        add(PlaceType.BOOK_STORE);
        add(PlaceType.LIBRARY);
        add(PlaceType.CAFE);
        add(PlaceType.UNIVERSITY);
        add(PlaceType.SCHOOL);
    }});*/
    Interest Machine = new Interest("Machinery", new ArrayList<String>() {{
        add(PlaceType.CAR_REPAIR);
        add(PlaceType.HARDWARE_STORE);
    }});
    Interest Magic = new Interest("Magic-Illusions", new ArrayList<String>() {{
        add(PlaceType.STADIUM);

        add(PlaceType.MUSEUM);
    }});
    Interest Marine_Bio = new Interest("Marine Biology", new ArrayList<String>() {{
        add(PlaceType.AQUARIUM);
        add(PlaceType.VETERINARY_CARE);
        add(PlaceType.PET_STORE);
        add(PlaceType.ZOO);

    }});
    Interest M_Life = new Interest("Married Life", new ArrayList<String>() {{
        add(PlaceType.JEWELRY_STORE);
        add(PlaceType.HOME_GOODS_STORE);
        add(PlaceType.RESTAURANT);
        add(PlaceType.REAL_ESTATE_AGENCY);

    }});
    Interest M_Arts = new Interest("Martial Arts", new ArrayList<String>() {{
        add(PlaceType.GYM);
    }});

    Interest M_Health = new Interest("Mental Health", new ArrayList<String>() {{
        add(PlaceType.DOCTOR);
        add(PlaceType.PHARMACY);
        add(PlaceType.PARK);
        add(PlaceType.SPA);
        add(PlaceType.HOSPITAL);
    }});
    Interest Weather = new Interest("Meteorology", new ArrayList<String>() {{
        add(PlaceType.PARK);
    }});
    Interest Metal = new Interest("Metallurgy", new ArrayList<String>() {{
        add(PlaceType.HARDWARE_STORE);

    }});
    Interest Movies = new Interest("Movies", new ArrayList<String>() {{
        add(PlaceType.MOVIE_RENTAL);
        add(PlaceType.MOVIE_THEATER);
        add(PlaceType.MEAL_DELIVERY);
    }});
    Interest Multimedia = new Interest("Multimedia", new ArrayList<String>() {{
        add(PlaceType.MOVIE_RENTAL);
        add(PlaceType.MOVIE_THEATER);
        add(PlaceType.LIBRARY);
        add(PlaceType.BOOK_STORE);
        add(PlaceType.UNIVERSITY);
        add(PlaceType.ART_GALLERY);
    }});
    Interest Music = new Interest("Music", new ArrayList<String>() {{
        add(PlaceType.BAR);
        add(PlaceType.STADIUM);
        add(PlaceType.NIGHT_CLUB);

    }});
    Interest Myth = new Interest("Mythology", new ArrayList<String>() {{
        add(PlaceType.BOOK_STORE);
        add(PlaceType.LIBRARY);
        add(PlaceType.CHURCH);
        add(PlaceType.HINDU_TEMPLE);
        add(PlaceType.MOSQUE);
        add(PlaceType.SYNAGOGUE);
    }});
    Interest Nature = new Interest("Nature", new ArrayList<String>() {{
        add(PlaceType.PARK);
        add(PlaceType.AQUARIUM);
        add(PlaceType.CAMPGROUND);

    }});
    Interest N_Life = new Interest("Nightlife", new ArrayList<String>() {{
        add(PlaceType.BAR);
        add(PlaceType.NIGHT_CLUB);
        add(PlaceType.BOWLING_ALLEY);
        add(PlaceType.ATM);
        add(PlaceType.UNIVERSITY);
        add(PlaceType.CASINO);
        add(PlaceType.LIQUOR_STORE);
    }});
    Interest Outdoors = new Interest("Outdoors", new ArrayList<String>() {{
        add(PlaceType.PARK);
        add(PlaceType.CAMPGROUND);
        add(PlaceType.RV_PARK);
        add(PlaceType.LODGING);
        add(PlaceType.BICYCLE_STORE);
        add(PlaceType.ZOO);
    }});
    Interest Parents = new Interest("Parenting", new ArrayList<String>() {{
        add(PlaceType.BOOK_STORE);
        add(PlaceType.SCHOOL);
        add(PlaceType.LIBRARY);

    }});
    Interest Pets = new Interest("Pets", new ArrayList<String>() {{
        add(PlaceType.PET_STORE);
        add(PlaceType.VETERINARY_CARE);
        add(PlaceType.PARK);
    }});
    Interest Philosophy = new Interest("Philosophy", new ArrayList<String>() {{
        add(PlaceType.BOOK_STORE);
        add(PlaceType.LIBRARY);
        add(PlaceType.CAFE);
        add(PlaceType.UNIVERSITY);
    }});
    Interest Photography = new Interest("Photography", new ArrayList<String>() {{
        add(PlaceType.PARK);
        add(PlaceType.ZOO);
        add(PlaceType.ART_GALLERY);
        add(PlaceType.MUSEUM);
        add(PlaceType.ELECTRONICS_STORE);
    }});
    Interest Poker = new Interest("Poker", new ArrayList<String>() {{
        add(PlaceType.ATM);
        add(PlaceType.BAR);
        add(PlaceType.CASINO);
        add(PlaceType.BOWLING_ALLEY);
    }});
    Interest Politics = new Interest("Politics", new ArrayList<String>() {{
        add(PlaceType.CITY_HALL);
        add(PlaceType.LOCAL_GOVERNMENT_OFFICE);
        add(PlaceType.LAWYER);
        add(PlaceType.POLICE);
    }});
    Interest Psych = new Interest("Psychology", new ArrayList<String>() {{
        add(PlaceType.HOSPITAL);
        add(PlaceType.DOCTOR);
        add(PlaceType.UNIVERSITY);
        add(PlaceType.BOOK_STORE);
    }});
    Interest Punk = new Interest("Punk Rock", new ArrayList<String>() {{
        add(PlaceType.BAR);
        add(PlaceType.CAFE);
        add(PlaceType.PARKING);
    }});

    Interest Relationships = new Interest("Relationships", new ArrayList<String>() {{
        add(PlaceType.CAFE);
        add(PlaceType.RESTAURANT);
        add(PlaceType.MOVIE_THEATER);
        add(PlaceType.NIGHT_CLUB);
    }});

    Interest Robotics = new Interest("Robotics", new ArrayList<String>() {{
        add(PlaceType.UNIVERSITY);
        add(PlaceType.ELECTRONICS_STORE);
        add(PlaceType.LIBRARY);
    }});

    Interest Rock_Music = new Interest("Rock Music", new ArrayList<String>() {{
        add(PlaceType.UNIVERSITY);
        add(PlaceType.BAR);
        add(PlaceType.BOWLING_ALLEY);
        add(PlaceType.STADIUM);

    }});

    Interest Sci = new Interest("Science", new ArrayList<String>() {{
        add(PlaceType.UNIVERSITY);
        add(PlaceType.SCHOOL);
        add(PlaceType.AQUARIUM);
        add(PlaceType.BOOK_STORE);
        add(PlaceType.LIBRARY);

    }});

    Interest Sci_Fi = new Interest(MyApplication.resources.getString(R.string.scifi), new ArrayList<String>() {{
        add(PlaceType.BOOK_STORE);
        add(PlaceType.LIBRARY);
        add(PlaceType.MOVIE_THEATER);
    }});

    Interest Sculpt = new Interest("Sculpting", new ArrayList<String>() {{
        add(PlaceType.PARK);
        add(PlaceType.MUSEUM);
        add(PlaceType.ART_GALLERY);
        add(PlaceType.PAINTER);
    }});

    Interest Space = new Interest("Space", new ArrayList<String>() {{
        add(PlaceType.UNIVERSITY);
        add(PlaceType.LIBRARY);
        add(PlaceType.BOOK_STORE);
    }});

    Interest Spirit = new Interest("Spirituality", new ArrayList<String>() {{
        add(PlaceType.BOOK_STORE);
        add(PlaceType.LIBRARY);
        add(PlaceType.CHURCH);
        add(PlaceType.HINDU_TEMPLE);
        add(PlaceType.MOSQUE);
        add(PlaceType.SYNAGOGUE);
    }});

    Interest Sports = new Interest("Sports (General)", new ArrayList<String>() {{
        add(PlaceType.BAR);
        add(PlaceType.CASINO);
        add(PlaceType.GYM);
        add(PlaceType.STADIUM);
        add(PlaceType.UNIVERSITY);
        add(PlaceType.PARK);
    }});

    Interest Tech = new Interest("Technology", new ArrayList<String>() {{
        add(PlaceType.UNIVERSITY);
        add(PlaceType.ELECTRONICS_STORE);

        add(PlaceType.CAFE);
    }});

    Interest TV = new Interest("Television", new ArrayList<String>() {{
        add(PlaceType.MOVIE_RENTAL);
        add(PlaceType.ELECTRONICS_STORE);

    }});

    Interest Trains = new Interest("Trains-Railroads", new ArrayList<String>() {{
        add(PlaceType.TRAIN_STATION);
        add(PlaceType.TRANSIT_STATION);
        add(PlaceType.SUBWAY_STATION);
    }});

    Interest Travel = new Interest("Travel", new ArrayList<String>() {{
        add(PlaceType.AIRPORT);
        add(PlaceType.CAR_RENTAL);
        add(PlaceType.SUBWAY_STATION);
        add(PlaceType.TRANSIT_STATION);
        add(PlaceType.TRAIN_STATION);
        add(PlaceType.TRAVEL_AGENCY);
        add(PlaceType.EMBASSY);
        add(PlaceType.TAXI_STAND);
        add(PlaceType.RV_PARK);
        add(PlaceType.LODGING);
        add(PlaceType.BUS_STATION);
    }});

    Interest Uni = new Interest("University-College", new ArrayList<String>() {{
        add(PlaceType.UNIVERSITY);
        add(PlaceType.LIBRARY);
        add(PlaceType.BOOK_STORE);

    }});

    Interest Video_Games = new Interest("Video Games", new ArrayList<String>() {{
        add(PlaceType.ELECTRONICS_STORE);
    }});

    Interest V_Cars = new Interest("Vintage Cars", new ArrayList<String>() {{
        add(PlaceType.CAR_RENTAL);
        add(PlaceType.CAR_REPAIR);
        add(PlaceType.CAR_WASH);
        add(PlaceType.MUSEUM);
    }});

    Interest VR = new Interest("Virtual Reality", new ArrayList<String>() {{
        add(PlaceType.ELECTRONICS_STORE);
    }});
    Interest Wine = new Interest("Wine", new ArrayList<String>() {{
        add(PlaceType.BAR);
        add(PlaceType.CAFE);
        add(PlaceType.LIQUOR_STORE);
        add(PlaceType.RESTAURANT);
    }});
    Interest Writing = new Interest("Writing", new ArrayList<String>() {{
        add(PlaceType.BOOK_STORE);
        add(PlaceType.LIBRARY);
        add(PlaceType.SCHOOL);
        add(PlaceType.UNIVERSITY);
    }});


}