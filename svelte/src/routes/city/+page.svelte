<script>
  import { appData } from "../store";

  $: currCity = JSON.parse($appData.currCity);
</script>

<h1>{currCity["display_name"]}</h1>
<h3>Latitude: {currCity["lat"]}</h3>
<h3>Longitude: {currCity["lon"]}</h3>
<p>Currently: {$appData.periods[0]["detailedForecast"]}</p>

<table>
  <thead>
    <tr>
      <th>Period</th>
      <th>Weather</th>
      <th>Temperature</th>
      <th>Wind</th>
    </tr>
  </thead>
  <tbody>
    {#each $appData.periods as { number, name, shortForecast, temperature, temperatureUnit, windDirection, windSpeed } (number)}
      <tr>
        <td>{name}</td>
        <td>{shortForecast}</td>
        <td>{`${temperature} ${temperatureUnit}`}</td>
        <td>{`${windDirection} ${windSpeed}`}</td>
      </tr>
    {/each}
  </tbody>
</table>

<style>
  table {
    width: 100%;
    border-collapse: collapse;
  }
  th,
  td {
    border: 1px solid #ddd;
    padding: 8px;
  }
  th {
    cursor: pointer;
    background-color: #f2f2f2;
  }
</style>
