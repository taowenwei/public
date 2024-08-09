<script>
  import { enhance } from "$app/forms";
  import { appData } from "./store";
  import { goto } from "$app/navigation";

  let selected;
</script>

<h1>Welcome to SvelteKit</h1>

<form
  method="POST"
  action="?/lookup"
  use:enhance={() =>
    async ({ result, update }) => {
      const {
        data: { cities },
      } = result;
      const { display_name, lat, lon } = cities[0];
      appData.update((data) => ({
        ...data,
        currCity: JSON.stringify({ display_name, lat, lon }),
        cities,
      }));
      await update();
    }}
>
  <label>
    Please enter a city name:
    <input name="city" autocomplete="off" />
  </label>
</form>

{#if $appData.cities.length > 0}
  <form
    method="POST"
    action="?/forecast"
    use:enhance={() =>
      async ({ result, update }) => {
        const {
          data: { periods },
        } = result;
        appData.update((data) => ({ ...data, periods }));
        await update();
        goto("/city");
      }}
  >
    <select
      name="geolocation"
      bind:value={selected}
      on:change={() =>
        appData.update((data) => ({ ...data, currCity: selected }))}
    >
      {#each $appData.cities as { place_id, display_name, lat, lon } (place_id)}
        <option value={JSON.stringify({ display_name, lat, lon })}>
          {display_name}
        </option>
      {/each}
    </select>

    <button type="submit"> Submit </button>
  </form>
{/if}
