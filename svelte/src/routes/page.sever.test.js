import { describe, it, expect, vi } from 'vitest';
import { actions } from './+page.server'; // Import the lookup function

describe('lookup function', () => {
	it('should fetch city data from OpenStreetMap API', async () => {
		const mockFormData = {
			get: vi.fn().mockReturnValue('Berlin') // Mocking the city name
		};
		const mockRequest = {
			formData: vi.fn().mockResolvedValue(mockFormData) // Mocking the request object
		};

		// Mocking the fetch function to return dummy data
		global.fetch = vi.fn().mockResolvedValue({
			json: vi.fn().mockResolvedValue([{ city: 'Berlin' }]) // Mocking the response data
		});

		const result = await actions.lookup({ request: mockRequest });

		expect(mockFormData.get).toHaveBeenCalledWith('city');
		expect(fetch).toHaveBeenCalledWith(
			'https://nominatim.openstreetmap.org/search?city=Berlin&format=json'
		);
		expect(result).toEqual({ success: true, cities: [{ city: 'Berlin' }] });
	});
});
