(function () {

  /**
   * Initializes the Leaflet map and renders markers for open nets
   *
   * @param {Array<Object>} openNets
   *        List of ghost net DTOs provided by Thymeleaf
   *        Expected properties:
   *        - id
   *        - latitude
   *        - longitude
   *        - status
   *        - reportedBy
   *        - estimatedSizeM2
   *        - assignedSalvagerUsername
   */
  function initOpenNetsMap(openNets) {

    // If the map container is not present, do nothing
    const mapEl = document.getElementById("map");
    if (!mapEl) return;

    // Create a world map centered roughly on Europe
    const map = L.map("map").setView([56, 4], 4);

    // Base map layer using OpenStreetMap tiles.
    L.tileLayer("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png", {
      maxZoom: 18,
      attribution: "&copy; OpenStreetMap contributors"
    }).addTo(map);

    // If there are no open nets, render a placeholder marker
    if (!openNets || openNets.length === 0) {
      L.marker([56.5, 4.0])
        .addTo(map)
        .bindPopup(
          "<strong>Example ghost net</strong><br/>North Sea placeholder"
        );
      return;
    }

    // Collect all coordinates to auto-fit the map bounds later
    const bounds = [];

    // Render one marker per ghost net
    openNets.forEach(net => {

      // Skip invalid or incomplete coordinate data
      if (net.latitude == null || net.longitude == null) return;

      const coords = [net.latitude, net.longitude];
      bounds.push(coords);

      // Normalize optional fields for robust rendering
      const id = (net.id != null) ? net.id : "-";
      const status = net.status || "REPORTED";
      const reporter = net.reportedBy || "anonymous";
      const size = (net.estimatedSizeM2 != null) ? net.estimatedSizeM2 : "-";
      const assigned = net.assignedSalvagerUsername || "Unassigned";

      // Uses escaped data from Thymeleaf
      const popupHtml =
        `<strong>Ghost net #${id}</strong><br/>` +
        `Lat: ${net.latitude}, Lon: ${net.longitude}<br/>` +
        `Reported by: ${reporter}<br/>` +
        `Estimated size (m²): ${size}<br/>` +
        `Status: ${status}<br/>` +
        `Assigned salvager: ${assigned}<br/><br/>` +
        `<button type="button" onclick="scrollToNet(${id})">Go to net</button>`;

      L.marker(coords)
        .addTo(map)
        .bindPopup(popupHtml);
    });

    // Automatically zoom and center the map
    if (bounds.length > 0) {
      map.fitBounds(bounds, { padding: [30, 30] });
    }
  }

  window.initOpenNetsMap = initOpenNetsMap;

})();
