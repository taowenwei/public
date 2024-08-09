/** @type {import('./$types').Actions} */
export const actions = {
  lookup: async ({ request }) => {
    const data = await request.formData();
    const city = data.get("city");
    const response = await fetch(
      `https://nominatim.openstreetmap.org/search?city=${city}&format=json`
    );
    const cities = await response.json();
    return { success: true, cities };
  },

  forecast: async ({ request }) => {
    const data = await request.formData();
    const geoString = data.get("geolocation");
    const { lat, lon } = JSON.parse(geoString);
    let response = await fetch(`https://api.weather.gov/points/${lat},${lon}`);
    const {
      properties: { forecast },
    } = await response.json();
    response = await fetch(forecast);
    const {
      properties: { periods },
    } = await response.json();
    return { success: true, periods };
  },
};
