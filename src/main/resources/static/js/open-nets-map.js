(function () {
  "use strict";

  /**
   * Initialize the Leaflet map and render markers for "open" nets.
   *
   * Called from index.html after Thymeleaf injects `openNets` into the page.
   *
   * Expected `openNets` fields (DTO-like):
   * - id: number
   * - latitude: number
   * - longitude: number
   * - status: string (e.g., "REPORTED", "PENDING")
   * - reportedBy: string
   * - estimatedSizeM2: number | null
   * - assignedSalvagerUsername: string | null
   *
   * @param {Array<Object>} openNets
   */
  function initOpenNetsMap(openNets) {
    const mapEl = document.getElementById("map");
    if (!mapEl) return;

    // Create the map instance. Default center is roughly Europe / North Sea region
    const map = L.map("map").setView([56, 4], 4);

    // Base layer using OpenStreetMap tiles
    L.tileLayer("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png", {
      maxZoom: 18,
      attribution: "&copy; OpenStreetMap contributors",
    }).addTo(map);

    // If there is no data, show a single placeholder marker
    if (!Array.isArray(openNets) || openNets.length === 0) {
      L.marker([56.5, 4.0])
        .addTo(map)
        .bindPopup("<strong>Example ghost net</strong><br/>North Sea placeholder");
      return;
    }

    // Collects marker coordinates and auto-fit the map view to them afterwards
    const bounds = [];

    // Render one marker per net
    openNets.forEach((net) => {
      // Skip entries with missing coordinates (cannot be placed on the map)
      if (!net || net.latitude == null || net.longitude == null) return;

      const coords = [Number(net.latitude), Number(net.longitude)];
      if (Number.isNaN(coords[0]) || Number.isNaN(coords[1])) return;

      bounds.push(coords);

      // Normalize optional fields so the popup never renders "undefined"
      const id = net.id != null ? net.id : "-";
      const status = net.status || "REPORTED";
      const reporter = net.reportedBy || "anonymous";
      const size = net.estimatedSizeM2 != null ? net.estimatedSizeM2 : "-";
      const assigned = net.assignedSalvagerUsername || "Unassigned";

      // Data comes from server-side rendering and is still treated as untrusted input
      const popupHtml =
        `<strong>Ghost net #${id}</strong><br/>` +
        `Lat: ${coords[0]}, Lon: ${coords[1]}<br/>` +
        `Reported by: ${reporter}<br/>` +
        `Estimated size (m²): ${size}<br/>` +
        `Status: ${status}<br/>` +
        `Assigned salvager: ${assigned}<br/><br/>` +
        // scrollToNet() is defined on window in index.html
        `<button type="button" onclick="scrollToNet(${id})">Go to net</button>`;

      L.marker(coords).addTo(map).bindPopup(popupHtml);
    });

    if (bounds.length > 0) {
      map.fitBounds(bounds, { padding: [30, 30] });
    }
  }

  window.initOpenNetsMap = initOpenNetsMap;
})();
