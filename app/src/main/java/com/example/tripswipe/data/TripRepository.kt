package com.example.tripswipe.data

import com.example.tripswipe.R
import com.example.tripswipe.model.Attraction
import com.example.tripswipe.model.City

object TripRepository {
    private val placesPhotoService = PlacesPhotoService()
    private val attractionPlaceholder = listOf(R.drawable.placeholder_photo)

    private val baseCities = listOf(
        City("Los Angeles", "Sun, stories, and iconic spots", fallbackImageRes = R.drawable.placeholder_photo),
        City("Miami", "Beaches, art, and tropical color", fallbackImageRes = R.drawable.placeholder_photo),
        City("New York", "Skyline, culture, and classics", fallbackImageRes = R.drawable.placeholder_photo)
    )

    private val baseAttractions = listOf(
        Attraction(
            1, "Los Angeles", "Getty Center", "Museum",
            "A hilltop museum with art collections, gardens, and sweeping city views.",
            fallbackImageResIds = attractionPlaceholder,
            websiteUrl = "https://www.getty.edu/visit/center/",
            mapUrl = "https://maps.google.com/?q=Getty+Center+Los+Angeles"
        ),
        Attraction(
            2, "Los Angeles", "Griffith Observatory", "Park",
            "A scenic observatory near Griffith Park with skyline views and astronomy exhibits.",
            fallbackImageResIds = attractionPlaceholder,
            websiteUrl = "https://griffithobservatory.org/",
            mapUrl = "https://maps.google.com/?q=Griffith+Observatory+Los+Angeles"
        ),
        Attraction(
            3, "Los Angeles", "Santa Monica Pier", "Amusement",
            "A classic California boardwalk experience with rides, ocean air, and sunset views.",
            fallbackImageResIds = attractionPlaceholder,
            websiteUrl = "https://www.santamonicapier.org/",
            mapUrl = "https://maps.google.com/?q=Santa+Monica+Pier"
        ),
        Attraction(
            4, "Los Angeles", "Grand Central Market", "Restaurant",
            "A lively downtown food hall with a mix of local vendors and global flavors.",
            fallbackImageResIds = attractionPlaceholder,
            websiteUrl = "https://grandcentralmarket.com/",
            mapUrl = "https://maps.google.com/?q=Grand+Central+Market+Los+Angeles"
        ),
        Attraction(
            5, "Los Angeles", "Universal Studios Hollywood", "Amusement",
            "A movie-inspired theme park with rides, studio experiences, and entertainment.",
            fallbackImageResIds = attractionPlaceholder,
            websiteUrl = "https://www.universalstudioshollywood.com/",
            mapUrl = "https://maps.google.com/?q=Universal+Studios+Hollywood"
        ),
        Attraction(
            6, "Los Angeles", "The Broad", "Museum",
            "A contemporary art museum known for modern collections and an iconic honeycomb exterior.",
            fallbackImageResIds = attractionPlaceholder,
            websiteUrl = "https://www.thebroad.org/",
            mapUrl = "https://maps.google.com/?q=The+Broad+Los+Angeles"
        ),
        Attraction(
            7, "Los Angeles", "LACMA", "Museum",
            "A major art museum campus with diverse exhibitions and the famous Urban Light installation.",
            fallbackImageResIds = attractionPlaceholder,
            websiteUrl = "https://www.lacma.org/",
            mapUrl = "https://maps.google.com/?q=LACMA+Los+Angeles"
        ),
        Attraction(
            23, "Miami", "Pérez Art Museum Miami", "Museum",
            "A modern waterfront art museum featuring contemporary international works.",
            fallbackImageResIds = attractionPlaceholder,
            websiteUrl = "https://www.pamm.org/",
            mapUrl = "https://maps.google.com/?q=Perez+Art+Museum+Miami",
            imageSearchQuery = "Perez Art Museum Miami"
        ),
        Attraction(
            24, "Miami", "South Pointe Park", "Park",
            "A breezy waterfront park with walking paths, skyline views, and ocean scenery.",
            fallbackImageResIds = attractionPlaceholder,
            websiteUrl = "https://www.miamibeachfl.gov/city-hall/parks-and-recreation/parks-facilities-directory/south-pointe-park/",
            mapUrl = "https://maps.google.com/?q=South+Pointe+Park+Miami+Beach"
        ),
        Attraction(
            25, "Miami", "Wynwood Walls", "Amusement",
            "An outdoor street art destination filled with murals, color, and creative energy.",
            fallbackImageResIds = attractionPlaceholder,
            websiteUrl = "https://thewynwoodwalls.com/",
            mapUrl = "https://maps.google.com/?q=Wynwood+Walls+Miami"
        ),
        Attraction(
            26, "Miami", "Versailles Restaurant", "Restaurant",
            "A famous Cuban restaurant known for coffee, pastries, and classic dishes.",
            fallbackImageResIds = attractionPlaceholder,
            websiteUrl = "https://versaillesrestaurant.com/",
            mapUrl = "https://maps.google.com/?q=Versailles+Restaurant+Miami"
        ),
        Attraction(
            27, "Miami", "Vizcaya Museum & Gardens", "Museum",
            "A historic estate with ornate rooms, gardens, and bayfront views.",
            fallbackImageResIds = attractionPlaceholder,
            websiteUrl = "https://vizcaya.org/",
            mapUrl = "https://maps.google.com/?q=Vizcaya+Museum+and+Gardens"
        ),
        Attraction(
            28, "Miami", "Phillip and Patricia Frost Museum of Science", "Museum",
            "A science museum with an aquarium, planetarium, and interactive exhibits.",
            fallbackImageResIds = attractionPlaceholder,
            websiteUrl = "https://www.frostscience.org/",
            mapUrl = "https://maps.google.com/?q=Frost+Museum+of+Science+Miami"
        ),
        Attraction(
            29, "Miami", "Bayside Marketplace", "Restaurant",
            "A bustling bayfront destination with shopping, dining, and live music.",
            fallbackImageResIds = attractionPlaceholder,
            websiteUrl = "https://www.baysidemarketplace.com/",
            mapUrl = "https://maps.google.com/?q=Bayside+Marketplace+Miami"
        ),
        Attraction(
            45, "New York", "The Metropolitan Museum of Art", "Museum",
            "One of the world’s best-known museums with art spanning centuries and cultures.",
            fallbackImageResIds = attractionPlaceholder,
            websiteUrl = "https://www.metmuseum.org/",
            mapUrl = "https://maps.google.com/?q=Metropolitan+Museum+of+Art+New+York"
        ),
        Attraction(
            46, "New York", "Central Park", "Park",
            "A legendary urban park with lakes, paths, lawns, and landmarks throughout.",
            fallbackImageResIds = attractionPlaceholder,
            websiteUrl = "https://www.centralparknyc.org/",
            mapUrl = "https://maps.google.com/?q=Central+Park+New+York"
        ),
        Attraction(
            47, "New York", "Coney Island Luna Park", "Amusement",
            "A nostalgic amusement destination with rides, games, and a boardwalk setting.",
            fallbackImageResIds = attractionPlaceholder,
            websiteUrl = "https://lunaparknyc.com/",
            mapUrl = "https://maps.google.com/?q=Luna+Park+Coney+Island"
        ),
        Attraction(
            48, "New York", "Katz's Delicatessen", "Restaurant",
            "An iconic New York deli known for classic sandwiches and old-school charm.",
            fallbackImageResIds = attractionPlaceholder,
            websiteUrl = "https://katzsdelicatessen.com/",
            mapUrl = "https://maps.google.com/?q=Katz%27s+Delicatessen+New+York"
        ),
        Attraction(
            49, "New York", "Statue of Liberty", "Amusement",
            "A world-famous landmark offering harbor views and a classic New York experience.",
            fallbackImageResIds = attractionPlaceholder,
            websiteUrl = "https://www.nps.gov/stli/index.htm",
            mapUrl = "https://maps.google.com/?q=Statue+of+Liberty"
        ),
        Attraction(
            50, "New York", "Times Square", "Amusement",
            "A bright, high-energy crossroads lined with theaters, billboards, and nonstop action.",
            fallbackImageResIds = attractionPlaceholder,
            websiteUrl = "https://www.timessquarenyc.org/",
            mapUrl = "https://maps.google.com/?q=Times+Square+New+York"
        ),
        Attraction(
            51, "New York", "American Museum of Natural History", "Museum",
            "A major science museum known for dinosaurs, space, and natural wonders.",
            fallbackImageResIds = attractionPlaceholder,
            websiteUrl = "https://www.amnh.org/",
            mapUrl = "https://maps.google.com/?q=American+Museum+of+Natural+History"
        )
    )

    @Volatile
    private var cachedCities: List<City> = baseCities

    @Volatile
    private var cachedAttractionsById: Map<Int, Attraction> = baseAttractions.associateBy { it.id }

    suspend fun cities(): List<City> {
        val photoUrlsByQuery = placesPhotoService.photoUrlsForQueries(baseCities.map { it.imageSearchQuery }, limitPerQuery = 1)
        val enriched = baseCities.map { city ->
            city.copy(imageUrl = photoUrlsByQuery[city.imageSearchQuery]?.firstOrNull())
        }
        cachedCities = enriched
        return enriched
    }

    fun cachedCities(): List<City> = cachedCities

    suspend fun attractionsForCity(city: String): List<Attraction> {
        val localItems = baseAttractions.filter { it.city == city }
        val photoUrlsByQuery = placesPhotoService.photoUrlsForQueries(localItems.map { it.imageSearchQuery })
        val enriched = localItems.map { attraction ->
            attraction.copy(imageUrls = photoUrlsByQuery[attraction.imageSearchQuery].orEmpty())
        }

        val mergedById = cachedAttractionsById.toMutableMap()
        enriched.forEach { mergedById[it.id] = it }
        cachedAttractionsById = mergedById

        return enriched
    }

    fun cachedAttractionsForCity(city: String): List<Attraction> = cachedAttractionsById.values.filter { it.city == city }

    fun attractionById(id: Int): Attraction? = cachedAttractionsById[id] ?: baseAttractions.firstOrNull { it.id == id }

    fun categoriesFor(attractions: List<Attraction>): List<String> =
        listOf("All") + attractions.map { it.category }.distinct().sorted()
}
