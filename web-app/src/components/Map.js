import React from 'react';
import {Map, Marker, Popup, TileLayer} from 'react-leaflet';

import 'leaflet/dist/leaflet.css';
import "./Map.css";

export default function ({lat, lon, zoom}) {
    const position = [lat, lon];
    return (
        <Map center={position} zoom={zoom}>
            <TileLayer
                attribution='&amp;copy <a href="http://osm.org/copyright">OpenStreetMap</a> contributors'
                url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
            />
            <Marker position={position}>
                <Popup>A pretty CSS3 popup.<br/>Easily customizable.</Popup>
            </Marker>
        </Map>
    );
};

